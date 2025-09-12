package com.example.hunter_point.service;

import com.example.hunter_point.dto.response.DailyRevenueResponse;
import com.example.hunter_point.dto.response.MonthlyRevenueResponse;
import com.example.hunter_point.dto.response.TopAllocatorResponse;
import com.example.hunter_point.utils.response.ListResponse;

public interface DashboardService {
    ListResponse<TopAllocatorResponse> getTopMerchants();

    ListResponse<MonthlyRevenueResponse> getMonthlyRevenueThisYear();

    ListResponse<DailyRevenueResponse> getDailyRevenueThisMonth();
}
