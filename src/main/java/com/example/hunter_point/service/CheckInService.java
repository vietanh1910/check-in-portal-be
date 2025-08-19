package com.example.hunter_point.service;

import com.example.hunter_point.dto.response.CheckInResponse;

import java.util.List;

public interface CheckInService {
    List<CheckInResponse> getCheckInsByCampaign(Long campaignId);

    List<CheckInResponse> getCheckInsByUser(Long userId);

    CheckInResponse createCheckIn(Long userId, Long campaignId, Integer points, String verify);
}
