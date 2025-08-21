package com.example.hunter_point.service;

import com.example.hunter_point.dto.response.CheckInResponse;
import com.example.hunter_point.utils.response.ListResponse;
import com.example.hunter_point.utils.response.SimpleResponse;

public interface CheckInService {
    ListResponse<CheckInResponse> getCheckInsByCampaign(Long campaignId, int page, int size);

    ListResponse<CheckInResponse> getCheckInsByUser(Long userId, int page, int size);

    SimpleResponse createCheckIn(Long userId, Long campaignId, Integer points, String verify);
}
