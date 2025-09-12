package com.example.hunter_point.service.impl;

import com.example.hunter_point.dto.response.DailyRevenueResponse;
import com.example.hunter_point.dto.response.MonthlyRevenueResponse;
import com.example.hunter_point.dto.response.TopAllocatorResponse;
import com.example.hunter_point.repository.TransactionRepository;
import com.example.hunter_point.service.DashboardService;
import com.example.hunter_point.utils.response.GenerateResponse;
import com.example.hunter_point.utils.response.ListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final TransactionRepository transactionRepository;

    @Override
    public ListResponse<TopAllocatorResponse> getTopMerchants() {
        List<TopAllocatorResponse> topMerchants = transactionRepository.findTopMerchants();
        return GenerateResponse.generateSuccessListResponse(topMerchants, topMerchants.size());
    }

    @Override
    public ListResponse<MonthlyRevenueResponse> getMonthlyRevenueThisYear() {
        // Lấy dữ liệu raw từ repo (monthNumber, revenue)
        List<Object[]> rawData = transactionRepository.getMonthlyRevenueThisYear();

        // Map thành month -> revenue
        Map<Integer, Double> revenueMap = rawData.stream()
                .collect(Collectors.toMap(
                        r -> ((Number) r[0]).intValue(),   // month (1–12)
                        r -> ((Number) r[1]).doubleValue()
                ));

        // Danh sách đủ 12 tháng
        String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        List<MonthlyRevenueResponse> result = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            result.add(new MonthlyRevenueResponse(
                    months[i - 1],
                    revenueMap.getOrDefault(i, 0.0)
            ));
        }

        return GenerateResponse.generateSuccessListResponse(result, result.size());
    }

    @Override
    public ListResponse<DailyRevenueResponse> getDailyRevenueThisMonth() {
        // Lấy dữ liệu raw từ repo
        List<Object[]> rawData = transactionRepository.getDailyRevenueThisMonth();

        // Map ngày -> doanh thu
        Map<Integer, Double> revenueMap = rawData.stream()
                .collect(Collectors.toMap(
                        r -> ((Number) r[0]).intValue(),   // day of month
                        r -> ((Number) r[1]).doubleValue()
                ));

        // Tính số ngày trong tháng hiện tại
        YearMonth currentMonth = YearMonth.now();
        int daysInMonth = currentMonth.lengthOfMonth();

        // Build đủ ngày trong tháng
        List<DailyRevenueResponse> result = new ArrayList<>();
        for (int d = 1; d <= daysInMonth; d++) {
            result.add(new DailyRevenueResponse(
                    d,
                    revenueMap.getOrDefault(d, 0.0)
            ));
        }

        return GenerateResponse.generateSuccessListResponse(result, result.size());
    }
}
