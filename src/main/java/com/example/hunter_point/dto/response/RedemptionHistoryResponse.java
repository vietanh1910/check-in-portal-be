package com.example.hunter_point.dto.response;

import com.example.hunter_point.entity.enums.RedemptionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RedemptionHistoryResponse {

    private Long id;

    private VoucherResponse voucher;

    private LocalDateTime redeemedAt;

    private RedemptionStatus status;
}