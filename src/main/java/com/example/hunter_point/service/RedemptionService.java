package com.example.hunter_point.service;

import com.example.hunter_point.entity.RedemptionHistory;
import com.example.hunter_point.dto.response.RedemptionHistoryResponse;
import java.util.List;

public interface RedemptionService {
    List<RedemptionHistoryResponse> getMyRedemptionHistory(Long userId); // Lấy lịch sử vẫn dùng userId
    String generateQrTokenForRedemption(Long redemptionHistoryId, String userEmail); // <<-- SỬA Ở ĐÂY: Dùng email để xác thực
    RedemptionHistory confirmVoucherUsage(String qrToken);
}