package com.example.hunter_point.controller;

import com.example.hunter_point.dto.response.DailyRevenueResponse;
import com.example.hunter_point.dto.response.DashboardItemResponse;
import com.example.hunter_point.dto.response.MonthlyRevenueResponse;
import com.example.hunter_point.dto.response.TopAllocatorResponse;
import com.example.hunter_point.service.DashboardService;
import com.example.hunter_point.service.TransactionService;
import com.example.hunter_point.utils.response.GenerateResponse;
import com.example.hunter_point.utils.response.ListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/topMerchants")
    public ListResponse<TopAllocatorResponse> getTopMerchants() {
        try {
            return dashboardService.getTopMerchants();
        } catch (Exception e) {
            e.printStackTrace();
            return GenerateResponse.generateErrorListResponse();
        }
    }

    @GetMapping("/monthlyRevenues")
    public ListResponse<MonthlyRevenueResponse> getMonthlyRevenue() {
        try {
            return dashboardService.getMonthlyRevenueThisYear();
        } catch (Exception e) {
            e.printStackTrace();
            return GenerateResponse.generateErrorListResponse();
        }
    }

    @GetMapping("/dailyRevenues")
    public ListResponse<DailyRevenueResponse> getDailyRevenue() {
        try {
            return dashboardService.getDailyRevenueThisMonth();
        } catch (Exception e) {
            e.printStackTrace();
            return GenerateResponse.generateErrorListResponse();
        }
    }

    @GetMapping("/dashboardAdmin")
    public ListResponse<DashboardItemResponse> getDashboardAdmin() {
        try {
            return dashboardService.getDashboardAdmin();
        } catch (Exception e) {
            e.printStackTrace();
            return GenerateResponse.generateErrorListResponse();
        }
    }
}
