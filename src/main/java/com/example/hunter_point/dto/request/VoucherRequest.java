package com.example.hunter_point.dto.request;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VoucherRequest {
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
    private Boolean isPublished;
    private Long userId;
    private String imageUrl;
}

