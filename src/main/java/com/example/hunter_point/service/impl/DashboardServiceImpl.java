package com.example.hunter_point.service.impl;

import com.example.hunter_point.dto.response.DailyRevenueResponse;
import com.example.hunter_point.dto.response.DashboardItemResponse;
import com.example.hunter_point.dto.response.MonthlyRevenueResponse;
import com.example.hunter_point.dto.response.TopAllocatorResponse;
import com.example.hunter_point.entity.enums.ERole;
import com.example.hunter_point.entity.enums.UserStatus;
import com.example.hunter_point.repository.TransactionRepository;
import com.example.hunter_point.repository.UserRepository;
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
    private final UserRepository userRepository;

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

    @Override
    public ListResponse<DashboardItemResponse> getDashboardAdmin() {
        List<DashboardItemResponse> result = new ArrayList<>();

        // 1. Total Revenue
        Double totalRevenue = transactionRepository.sumTotalRevenue();
        Double lastMonthRevenue = transactionRepository.sumRevenueByMonth(YearMonth.now().minusMonths(1));
        double revenueGrowth = lastMonthRevenue != 0 ?
                ((totalRevenue - lastMonthRevenue) / lastMonthRevenue) * 100 : 0;

        result.add(new DashboardItemResponse(
                "Total Revenue",
                totalRevenue,
                String.format("%+.1f%% from last month", revenueGrowth)
        ));

        // 2. Current Month Revenue
        Double currentMonthRevenue = transactionRepository.sumRevenueByMonth(YearMonth.now());
        Double prevMonthRevenue = lastMonthRevenue;
        double growth = prevMonthRevenue != 0 ?
                ((currentMonthRevenue - prevMonthRevenue) / prevMonthRevenue) * 100 : 0;

        result.add(new DashboardItemResponse(
                "Current Month Revenue",
                currentMonthRevenue,
                String.format("%+.1f%% from last month", growth)
        ));

        // 3. Total Users
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countUserByStatus(UserStatus.ACTIVE);
        long inactiveUsers = userRepository.countUserByStatus(UserStatus.INACTIVE);

        result.add(new DashboardItemResponse(
                "Total Users",
                totalUsers,
                String.format("%,d active • %,d inactive", activeUsers, inactiveUsers)
        ));

        // 4. Total Merchants
        long totalMerchants = userRepository.countByRole(ERole.ALLOCATOR);
        long activeMerchants = userRepository.countByRoleAndStatus(ERole.ALLOCATOR, UserStatus.ACTIVE);
        long suspendedMerchants = userRepository.countByRoleAndStatus(ERole.ALLOCATOR, UserStatus.SUSPENDED);

        result.add(new DashboardItemResponse(
                "Total Merchants",
                totalMerchants,
                String.format("%,d active • %,d suspended", activeMerchants, suspendedMerchants)
        ));

        return GenerateResponse.generateSuccessListResponse(result, result.size());
    }
}
