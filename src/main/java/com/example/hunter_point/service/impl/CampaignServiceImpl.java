package com.example.hunter_point.service.impl;

import com.example.hunter_point.dto.LocationDTO;
import com.example.hunter_point.dto.WifiDTO;
import com.example.hunter_point.dto.request.CampaignRequest;
import com.example.hunter_point.dto.response.CampaignResponse;
import com.example.hunter_point.entity.Campaign;
import com.example.hunter_point.entity.Transaction;
import com.example.hunter_point.entity.User;
import com.example.hunter_point.entity.enums.CampaignStatus;
import com.example.hunter_point.entity.enums.ERole;
import com.example.hunter_point.entity.enums.TransactionStatus;
import com.example.hunter_point.entity.enums.TransactionType;
import com.example.hunter_point.exception.ResourceNotFoundException;
import com.example.hunter_point.repository.CampaignRepository;
import com.example.hunter_point.repository.TransactionRepository;
import com.example.hunter_point.repository.UserRepository;
import com.example.hunter_point.security.UserDetailsImpl;
import com.example.hunter_point.service.CampaignService;
import com.example.hunter_point.utils.response.GenerateResponse;
import com.example.hunter_point.utils.response.GetDetailResponse;
import com.example.hunter_point.utils.response.ListResponse;
import com.example.hunter_point.utils.response.SimpleResponse;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class CampaignServiceImpl implements CampaignService {

    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");


    public CampaignServiceImpl(CampaignRepository campaignRepository, UserRepository userRepository, TransactionRepository transactionRepository) {
        this.campaignRepository = campaignRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    // --- CREATE ---
    @Transactional
    @Override
    public SimpleResponse createCampaign(CampaignRequest requestDTO) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> userOptional = userRepository.findById(userDetails.getId());
        if (userOptional.isEmpty()) {
            return GenerateResponse.generateErrorSimpleResponse("User not found");
        }
        Campaign campaign = Campaign.builder()
                .allocator(userOptional.get())
                .name(requestDTO.getName())
                .description(requestDTO.getDescription())
                .locationName(requestDTO.getLocationName())
                .latitude(requestDTO.getLatitude())
                .longitude(requestDTO.getLongitude())
                .radiusMeters(requestDTO.getRadiusMeters() != null ? requestDTO.getRadiusMeters() : 50)
                .requiredWifiSsid(requestDTO.getRequiredWifiSsid())
                .requiredWifiBssid(requestDTO.getRequiredWifiBssid())
                .pointsPerCheckin(requestDTO.getPointsPerCheckin())
                .maxCheckinsPerUser(requestDTO.getMaxCheckinsPerUser() != null ? requestDTO.getMaxCheckinsPerUser() : 1)
                .totalBudget(requestDTO.getTotalBudget())
                .remainingBudget(requestDTO.getTotalBudget()) // Ban đầu bằng tổng budget
                .startDate(requestDTO.getStartDate())
                .endDate(requestDTO.getEndDate())
                .status(CampaignStatus.PENDING)
                .checkIns(0)
                .used(0)
                .build();

        campaign = campaignRepository.save(campaign);
        Transaction transaction = Transaction.builder()
                .type(TransactionType.SPENT)
                .point(requestDTO.getTotalBudget())
                .amount(requestDTO.getTotalBudget() / 100.0)
                .description(requestDTO.getName())
                .createdAt(LocalDateTime.now())
                .userId(userOptional.get().getId())
                .campaignId(campaign.getId())
                .build();
        transactionRepository.save(transaction);
        return GenerateResponse.generateSuccessSimpleResponse();
    }

    // --- READ ---
    @Override
    public GetDetailResponse<CampaignResponse> getCampaignById(Long id) {
        if (id == null) {
            return GenerateResponse.generateErrorGetDetailResponse("Campaign ID cannot be null");
        }
        Optional<Campaign> campaignOptional = campaignRepository.findById(id);
        if (campaignOptional.isEmpty()) {
            return GenerateResponse.generateErrorGetDetailResponse("Campaign not found with id: " + id);
        }
        Campaign campaign = campaignOptional.get();
        CampaignResponse campaignResponse = mapToResponseDTO(campaign);
        return GenerateResponse.generateSuccessGetDetailResponse(campaignResponse);
    }

    @Override
    public ListResponse<CampaignResponse> getAllCampaigns(CampaignRequest requestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Pageable pageable = PageRequest.of(requestDTO.getPage(), requestDTO.getSize());

        Page<Campaign> pageResult;

        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + ERole.ADMIN.name()))) {
            // ADMIN: Lấy tất cả
            pageResult = campaignRepository.findAll(pageable);

        } else if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + ERole.ALLOCATOR.name()))) {
            // ALLOCATOR: Lấy campaign do allocator này tạo
            pageResult = campaignRepository.findByAllocatorId(userDetails.getId(), pageable);

        } else {
            // USER: Tính khoảng cách -> xa dần
            var lat = requestDTO.getLatitude();
            var lon = requestDTO.getLongitude();

            if (lat == null || lon == null) {
                return GenerateResponse.generateErrorListResponse("Latitude and Longitude are required for USER role");
            }

            // repository query native SQL để sort theo khoảng cách
            pageResult = campaignRepository.findCampaignsNotCheckedInByUser(lat, lon, userDetails.getId(), pageable);
        }

        // map to response DTO
        List<CampaignResponse> responseList = pageResult.getContent()
                .stream()
                .sorted(Comparator.comparingInt(c -> c.getStatus().ordinal()))
                .map(this::mapToResponseDTO)
                .toList();

        return GenerateResponse.generateSuccessListResponse(
                responseList,
                pageResult.getTotalElements());
    }


    // --- UPDATE ---
    @Transactional
    @Override
    public SimpleResponse updateCampaign(Long id, CampaignRequest requestDTO) {
        if (id == null) {
            return GenerateResponse.generateErrorSimpleResponse("Campaign ID cannot be null");
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> userOptional = userRepository.findById(userDetails.getId());
        if (userOptional.isEmpty()) {
            return GenerateResponse.generateErrorSimpleResponse("User not found");
        }

        Optional<Campaign> campaignOptional = campaignRepository.findById(id);
        if (campaignOptional.isEmpty()) {
            return GenerateResponse.generateErrorSimpleResponse("Campaign not found with id: " + id);
        }
        Campaign campaign = campaignOptional.get();

        // Cập nhật các trường
        campaign.setName(requestDTO.getName());
        campaign.setDescription(requestDTO.getDescription());
        campaign.setLocationName(requestDTO.getLocationName());
        campaign.setLatitude(requestDTO.getLatitude());
        campaign.setLongitude(requestDTO.getLongitude());
        campaign.setRadiusMeters(requestDTO.getRadiusMeters());

        campaign.setRequiredWifiSsid(requestDTO.getRequiredWifiSsid());
        campaign.setRequiredWifiBssid(requestDTO.getRequiredWifiBssid());
        campaign.setPointsPerCheckin(requestDTO.getPointsPerCheckin());
        campaign.setMaxCheckinsPerUser(requestDTO.getMaxCheckinsPerUser());
        campaign.setTotalBudget(requestDTO.getTotalBudget());
        campaign.setStartDate(requestDTO.getStartDate());
        campaign.setEndDate(requestDTO.getEndDate());

        campaignRepository.save(campaign);
        Optional<Transaction> transactionOptional = transactionRepository.findByCampaignId(id);
        if (transactionOptional.isEmpty()) {
            Transaction transaction = Transaction.builder()
                    .type(TransactionType.SPENT)
                    .point(requestDTO.getTotalBudget())
                    .amount(requestDTO.getTotalBudget() / 100.0)
                    .description(requestDTO.getName())
                    .createdAt(LocalDateTime.now())
                    .userId(userOptional.get().getId())
                    .campaignId(id)
                    .build();
            transactionRepository.save(transaction);
        } else {
            Transaction transaction = transactionOptional.get();
            transaction.setPoint(requestDTO.getTotalBudget());
            transaction.setAmount(requestDTO.getTotalBudget() / 100.0);
            transaction.setDescription(requestDTO.getDescription());
            transactionRepository.save(transaction);
        }
        return GenerateResponse.generateSuccessSimpleResponse();
    }

    // --- DELETE ---
    @Override
    public SimpleResponse deleteCampaign(Long id) {
        if (id == null) {
            return GenerateResponse.generateErrorSimpleResponse("Campaign ID cannot be null");
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> userOptional = userRepository.findById(userDetails.getId());
        if (userOptional.isEmpty()) {
            return GenerateResponse.generateErrorSimpleResponse("User not found");
        }

        Optional<Campaign> campaignOptional = campaignRepository.findById(id);
        if (campaignOptional.isEmpty()) {
            return GenerateResponse.generateErrorSimpleResponse("Campaign not found with id: " + id);
        }
        Campaign campaign = campaignOptional.get();
        campaign.setStatus(CampaignStatus.CANCELLED);
        campaign.setUpdatedAt(LocalDateTime.now());
        campaignRepository.save(campaign);
        return GenerateResponse.generateSuccessSimpleResponse();
    }


    // --- Helper Method để map Entity sang DTO ---
    private CampaignResponse mapToResponseDTO(Campaign campaign) {
        // TODO: Tính toán các giá trị used và checkIns sau khi có bảng checkin
        long used = 0; // Placeholder
        long checkIns = 0; // Placeholder
        String qrUrl = "https://api.example.com/campaigns/" + campaign.getId() + "/qr"; // Placeholder QR URL

        return CampaignResponse.builder()
                .id(campaign.getId())
                .name(campaign.getName())
                .description(campaign.getDescription())
                .startDate(campaign.getStartDate().format(DATE_FORMATTER))
                .endDate(campaign.getEndDate().format(DATE_FORMATTER))
                .startTime(campaign.getStartDate().format(TIME_FORMATTER))
                .endTime(campaign.getEndDate().format(TIME_FORMATTER))
                .locationName(campaign.getLocationName())
                .location(new LocationDTO(campaign.getLatitude(), campaign.getLongitude()))
                .rewardPerCheckin(campaign.getPointsPerCheckin())
                .pointBudget(campaign.getTotalBudget())
                .wifi(new WifiDTO(campaign.getRequiredWifiSsid(), campaign.getRequiredWifiBssid()))
                .qrUrl(qrUrl)
                .used(used)
                .checkIns(checkIns)
                .status(campaign.getStatus())
                .createdAt(campaign.getCreatedAt().format(DATETIME_FORMATTER))
                .updatedAt(campaign.getUpdatedAt().format(DATETIME_FORMATTER))
                .radiusMeters(campaign.getRadiusMeters())
                .build();
    }

    @Override
    public Long getAllocatorIdByCampaignId(Long campaignId) {
        // Lấy entity từ repository
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with id " + campaignId));
        return campaign.getAllocator().getId(); // giả sử Campaign có field allocator kiểu User
    }

    @Override
    public SimpleResponse approveCampaign(Long id) {
        if (id == null) {
            return GenerateResponse.generateErrorSimpleResponse("Campaign ID cannot be null");
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> userOptional = userRepository.findById(userDetails.getId());
        if (userOptional.isEmpty()) {
            return GenerateResponse.generateErrorSimpleResponse("User not found");
        }
        Optional<Campaign> campaignOptional = campaignRepository.findById(id);
        if (campaignOptional.isEmpty()) {
            return GenerateResponse.generateErrorSimpleResponse("Campaign not found with id: " + id);
        }
        Campaign campaign = campaignOptional.get();
        campaign.setStatus(CampaignStatus.APPROVED);
        campaign.setApprovedAt(LocalDateTime.now());
        campaign.setApprovedBy(userOptional.get());
        campaignRepository.save(campaign);
        Optional<Transaction> transactionOptional = transactionRepository.findByCampaignId(id);
        if (transactionOptional.isEmpty()) {
            return GenerateResponse.generateErrorSimpleResponse("Transaction not found");
        } else {
            Transaction transaction = transactionOptional.get();
            transaction.setApprovedAt(LocalDateTime.now());
            transaction.setStatus(TransactionStatus.WITHDRAWN);
            transaction.setApprovedBy(userOptional.get().getId());
            transactionRepository.save(transaction);
        }
        return GenerateResponse.generateSuccessSimpleResponse();
    }
}
