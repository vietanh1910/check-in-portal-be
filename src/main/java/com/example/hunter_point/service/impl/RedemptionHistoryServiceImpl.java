package com.example.hunter_point.service.impl;
import com.example.hunter_point.dto.response.RedemptionHistoryResponse;
import com.example.hunter_point.dto.response.VoucherResponse;
import com.example.hunter_point.entity.RedemptionHistory;
import com.example.hunter_point.entity.Voucher;
import com.example.hunter_point.repository.RedemptionHistoryRepository;
import com.example.hunter_point.service.RedemptionHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RedemptionHistoryServiceImpl implements RedemptionHistoryService {

    private final RedemptionHistoryRepository redemptionHistoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RedemptionHistoryResponse> getHistoryForUser(Long userId) {
        // 1. Lấy danh sách các bản ghi lịch sử từ database
        List<RedemptionHistory> historyEntities = redemptionHistoryRepository.findByUserIdOrderByRedeemedAtDesc(userId);

        // 2. Chuyển đổi (map) từng Entity sang DTO để trả về cho client
        return historyEntities.stream()
                .map(this::mapToHistoryResponse)
                .collect(Collectors.toList());
    }

    // Hàm trợ giúp để chuyển đổi một RedemptionHistory entity sang DTO
    private RedemptionHistoryResponse mapToHistoryResponse(RedemptionHistory history) {
        // Chuyển đổi Voucher entity bên trong sang VoucherResponse DTO
        VoucherResponse voucherDto = mapVoucherToResponse(history.getVoucher());

        return RedemptionHistoryResponse.builder()
                .id(history.getId())
                .redeemedAt(history.getRedeemedAt())
                .status(history.getStatus())
                .voucher(voucherDto)
                .build();
    }

    // Hàm trợ giúp để chuyển đổi Voucher entity sang VoucherResponse DTO
    // (Bạn có thể đặt hàm này ở một lớp tiện ích chung nếu muốn)
    private VoucherResponse mapVoucherToResponse(Voucher v) {
        return VoucherResponse.builder()
                .id(v.getId())
                .title(v.getTitle())
                .description(v.getDescription())
                .discountType(v.getDiscountType())
                .discountValue(v.getDiscountValue())
                .minOrderValue(v.getMinOrderValue())
                .maxDiscount(v.getMaxDiscount())
                .pointCost(v.getPointCost())
                .quantity(v.getQuantity())
                .claimed(v.getClaimed())
                .startDate(v.getStartDate())
                .endDate(v.getEndDate())
                .status(v.getStatus())
                .isPublished(v.getIsPublished())
                .userId(v.getUserId())
                .createdAt(v.getCreatedAt())
                .updatedAt(v.getUpdatedAt())
                .imageUrl(v.getImageUrl())
                .build();
    }
}