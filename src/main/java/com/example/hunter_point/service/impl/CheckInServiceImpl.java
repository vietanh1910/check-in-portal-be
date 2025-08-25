package com.example.hunter_point.service.impl;

import com.example.hunter_point.dto.response.CampaignResponse;
import com.example.hunter_point.dto.response.CheckInResponse;
import com.example.hunter_point.entity.Campaign;
import com.example.hunter_point.entity.CheckIn;
import com.example.hunter_point.entity.User;
import com.example.hunter_point.repository.CampaignRepository;
import com.example.hunter_point.repository.CheckInRepository;
import com.example.hunter_point.repository.UserRepository;
import com.example.hunter_point.service.CheckInService;
import com.example.hunter_point.utils.response.GenerateResponse;
import com.example.hunter_point.utils.response.ListResponse;
import com.example.hunter_point.utils.response.SimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CheckInServiceImpl implements CheckInService {

    private final CheckInRepository checkInRepository;
    private final UserRepository userRepository;
    private final CampaignRepository campaignRepository;

    @Override
    public ListResponse<CheckInResponse> getCheckInsByCampaign(Long campaignId, int page, int size) {
        if (campaignId == null) {
            return GenerateResponse.generateErrorListResponse("Campaign ID cannot be null");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<CheckIn> pageResult = checkInRepository.findByCampaignId(campaignId, pageable);
        List<CheckInResponse> responseList = pageResult.getContent()
                .stream()
                .map(this::mapToDto)
                .toList();
        return GenerateResponse.generateSuccessListResponse(
                responseList,
                pageResult.getTotalElements());
    }

    @Override
    public ListResponse<CheckInResponse> getCheckInsByUser(Long userId, int page, int size) {
        if (userId == null) {
            return GenerateResponse.generateErrorListResponse("User ID cannot be null");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<CheckIn> pageResult = checkInRepository.findByUserId(userId, pageable);
        List<CheckInResponse> responseList = pageResult.getContent()
                .stream()
                .map(this::mapToDto)
                .toList();
        return GenerateResponse.generateSuccessListResponse(
                responseList,
                pageResult.getTotalElements());
    }

    @Override
    @Transactional
    public SimpleResponse createCheckIn(Long userId, Long campaignId, Integer points, String verify) {
        // Lấy user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Lấy campaign
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        LocalDateTime today = LocalDateTime.now();

        // 1. Validate thời gian campaign
        if (campaign.getStartDate() != null && today.isBefore(campaign.getStartDate())) {
            throw new RuntimeException("Campaign has not started yet");
        }
        if (campaign.getEndDate() != null && today.isAfter(campaign.getEndDate())) {
            throw new RuntimeException("Campaign has expired");
        }

        // 2. Validate số lượt check-in
        if (campaign.getMaxCheckinsPerUser() == 0) {
            throw new RuntimeException("Campaign check-in limit reached");
        }

        // 3. Update campaign (tăng lượt đã dùng)
        campaign.setCheckIns(campaign.getCheckIns() + 1);

        // 4. Update user (cộng điểm)
        user.setPoints(user.getPoints() + points);

        // 5. Tạo check-in record
        CheckIn checkIn = CheckIn.builder()
                .user(user)
                .campaign(campaign)
                .pointsEarned(points)
                .verify(verify)
                .build();

        checkInRepository.save(checkIn);
        userRepository.save(user);
        campaignRepository.save(campaign);

        return GenerateResponse.generateSuccessSimpleResponse();
    }


    private CheckInResponse mapToDto(CheckIn checkIn) {
        return CheckInResponse.builder()
                .id(checkIn.getId())
                .userId(checkIn.getUser().getId())
                .userName(checkIn.getUser().getFullName()) // giả sử User có fullName
                .campaignId(checkIn.getCampaign().getId())
                .checkInTime(checkIn.getCheckInTime())
                .pointsEarned(checkIn.getPointsEarned())
                .verify(checkIn.getVerify())
                .build();
    }
}
