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
    public SimpleResponse createCheckIn(Long userId, Long campaignId, Integer points, String verify) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        CheckIn checkIn = CheckIn.builder()
                .user(user)
                .campaign(campaign)
                .pointsEarned(points)
                .verify(verify)
                .build();

        checkInRepository.save(checkIn);
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
