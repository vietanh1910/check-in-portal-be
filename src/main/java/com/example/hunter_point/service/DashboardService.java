package com.example.hunter_point.service;

import com.example.hunter_point.dto.response.*;
import com.example.hunter_point.utils.response.ListResponse;

public interface DashboardService {
    ListResponse<TopAllocatorResponse> getTopMerchants();

    ListResponse<MonthlyRevenueResponse> getMonthlyRevenueThisYear();

    ListResponse<DailyRevenueResponse> getDailyRevenueThisMonth();

    ListResponse<DashboardItemResponse> getDashboardAdmin();

    ListResponse<DailyCheckinResponse> getDailyCheckinsByAllocator(Long allocatorId);

    ListResponse<CampaignPointResponse> getCampaignPoints(Long allocatorId);
}
