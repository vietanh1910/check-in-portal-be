package com.example.hunter_point.service.impl;

import com.example.hunter_point.dto.request.CampaignRequest;
import com.example.hunter_point.dto.response.CampaignResponse;
import com.example.hunter_point.dto.LocationDTO;
import com.example.hunter_point.dto.WifiDTO;
import com.example.hunter_point.entity.Campaign;
import com.example.hunter_point.entity.User;
import com.example.hunter_point.entity.enums.CampaignStatus;
import com.example.hunter_point.exception.ResourceNotFoundException;
import com.example.hunter_point.repository.CampaignRepository;
import com.example.hunter_point.repository.UserRepository;
import com.example.hunter_point.service.CampaignService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampaignServiceImpl implements CampaignService {

    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;

    // Định dạng ngày giờ theo yêu cầu của FE
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");


    public CampaignServiceImpl(CampaignRepository campaignRepository, UserRepository userRepository) {
        this.campaignRepository = campaignRepository;
        this.userRepository = userRepository;
    }

    // --- CREATE ---
    @Transactional
    @Override
    public CampaignResponse createCampaign(CampaignRequest requestDTO, Long allocatorId) {
        User allocator = userRepository.findById(allocatorId)
                .orElseThrow(() -> new ResourceNotFoundException("Allocator not found with id: " + allocatorId));

        Campaign campaign = Campaign.builder()
                .allocator(allocator)
                .name(requestDTO.getName())
                .description(requestDTO.getDescription())
                .locationName(requestDTO.getLocationName())
                .latitude(requestDTO.getLatitude())
                .longitude(requestDTO.getLongitude())
                .radiusMeters(requestDTO.getRadiusMeters() != null ? requestDTO.getRadiusMeters() : 50)
                .requiredWifiSsid(requestDTO.getRequiredWifiSsid())
                .pointsPerCheckin(requestDTO.getPointsPerCheckin())
                .maxCheckinsPerUser(requestDTO.getMaxCheckinsPerUser() != null ? requestDTO.getMaxCheckinsPerUser() : 1)
                .totalBudget(requestDTO.getTotalBudget())
                .remainingBudget(requestDTO.getTotalBudget()) // Ban đầu bằng tổng budget
                .startDate(requestDTO.getStartDate())
                .endDate(requestDTO.getEndDate())
                .status(requestDTO.getStatus() != null ? requestDTO.getStatus() : CampaignStatus.PENDING)
                .build();

        Campaign savedCampaign = campaignRepository.save(campaign);
        return mapToResponseDTO(savedCampaign);
    }

    // --- READ ---
    @Override
    public CampaignResponse getCampaignById(Long id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with id: " + id));
        return mapToResponseDTO(campaign);
    }

    @Override
    public List<CampaignResponse> getAllCampaigns() {
        return campaignRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CampaignResponse> getCampaignsByAllocator(Long allocatorId) {
        if (!userRepository.existsById(allocatorId)) {
            throw new ResourceNotFoundException("Allocator not found with id: " + allocatorId);
        }
        return campaignRepository.findByAllocatorId(allocatorId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // --- UPDATE ---
    @Transactional
    @Override
    public CampaignResponse updateCampaign(Long id, CampaignRequest requestDTO) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with id: " + id));

        // Cập nhật các trường
        campaign.setName(requestDTO.getName());
        campaign.setDescription(requestDTO.getDescription());
        campaign.setLocationName(requestDTO.getLocationName());
        campaign.setLatitude(requestDTO.getLatitude());
        campaign.setLongitude(requestDTO.getLongitude());
        // ... cập nhật các trường khác tương tự

        Campaign updatedCampaign = campaignRepository.save(campaign);
        return mapToResponseDTO(updatedCampaign);
    }

    // --- DELETE ---
    @Override
    public void deleteCampaign(Long id) {
        if (!campaignRepository.existsById(id)) {
            throw new ResourceNotFoundException("Campaign not found with id: " + id);
        }
        campaignRepository.deleteById(id);
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
                .location(new LocationDTO(campaign.getLatitude(), campaign.getLongitude()))
                .rewardPerCheckin(campaign.getPointsPerCheckin())
                .pointBudget(campaign.getTotalBudget())
                .wifi(new WifiDTO(campaign.getRequiredWifiSsid(), "")) // BSSID không có trong DB
                .qrUrl(qrUrl)
                .used(used)
                .checkIns(checkIns)
                .status(campaign.getStatus())
                .createdAt(campaign.getCreatedAt().format(DATETIME_FORMATTER))
                .updatedAt(campaign.getUpdatedAt().format(DATETIME_FORMATTER))
                .build();
    }

    @Override
    public Long getAllocatorIdByCampaignId(Long campaignId) {
        // Lấy entity từ repository
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign not found with id " + campaignId));
        return campaign.getAllocator().getId(); // giả sử Campaign có field allocator kiểu User
    }
}
