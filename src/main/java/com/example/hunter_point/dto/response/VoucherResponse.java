package com.example.hunter_point.dto.response;

import com.example.hunter_point.entity.enums.VoucherStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VoucherResponse {
    private Long id;
    private String title;
    private String description;
    private String discountType;
    private Integer discountValue;
    private Integer minOrderValue;
    private Integer maxDiscount;
    private Integer pointCost;
    private Integer quantity;
    private Integer claimed;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private VoucherStatus status;
    private Boolean isPublished;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

