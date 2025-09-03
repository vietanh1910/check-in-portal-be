package com.example.hunter_point.service;

import com.example.hunter_point.dto.response.RedemptionHistoryResponse;
import java.util.List;

public interface RedemptionHistoryService {
    List<RedemptionHistoryResponse> getHistoryForUser(Long userId);
}