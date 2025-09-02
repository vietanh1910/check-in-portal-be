package com.example.hunter_point.entity;

import com.example.hunter_point.entity.enums.VoucherStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "vouchers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String description;

    private String discountType;   // percent, fixed

    private Integer discountValue;

    private Integer minOrderValue;

    private Integer maxDiscount;

    private Integer pointCost;

    private Integer quantity;

    private Integer claimed;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private VoucherStatus status;        // active, inactive...

    private Boolean isPublished;

    private Long userId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    private String imageUrl;
}

