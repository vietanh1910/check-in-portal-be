package com.example.hunter_point.service.impl;

import com.example.hunter_point.dto.response.CheckInResponse;
import com.example.hunter_point.entity.Campaign;
import com.example.hunter_point.entity.CheckIn;
import com.example.hunter_point.entity.User;
import com.example.hunter_point.repository.CampaignRepository;
import com.example.hunter_point.repository.CheckInRepository;
import com.example.hunter_point.repository.UserRepository;
import com.example.hunter_point.service.CheckInService;
import lombok.RequiredArgsConstructor;
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
    public List<CheckInResponse> getCheckInsByCampaign(Long campaignId) {
        return checkInRepository.findByCampaignId(campaignId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CheckInResponse> getCheckInsByUser(Long userId) {
        return checkInRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CheckInResponse createCheckIn(Long userId, Long campaignId, Integer points, String verify) {
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

        CheckIn saved = checkInRepository.save(checkIn);
        return mapToDto(saved);
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
