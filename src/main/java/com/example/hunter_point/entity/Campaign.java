package com.example.hunter_point.entity;

import com.example.hunter_point.entity.enums.CampaignStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "campaigns")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "allocator_id", nullable = false)
    private User allocator;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    // location
    private String locationName;

    @Column(precision = 10, scale = 8, nullable = false)
    private BigDecimal latitude;

    @Column(precision = 11, scale = 8, nullable = false)
    private BigDecimal longitude;

    private Integer radiusMeters;

    // wifi
    private String requiredWifiSsid;
    private String requiredWifiBssid;

    // reward
    @Column(nullable = false)
    private Integer pointsPerCheckin; // map sang rewardPerCheckin

    private Integer maxCheckinsPerUser;

    @Column(nullable = false)
    private Integer totalBudget; // map sang pointBudget

    @Column(nullable = false)
    private Integer remainingBudget;

    // thời gian
    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    // thêm giờ riêng nếu FE cần
    private String startTime;
    private String endTime;

    // QR Code
    @Column(columnDefinition = "TEXT")
    private String qrUrl;

    // thống kê
    private Integer used = 0;      // số lần đã sử dụng
    private Integer checkIns = 0;  // số lượt checkin

    @Enumerated(EnumType.STRING)
    private CampaignStatus status;

    @Column(columnDefinition = "TEXT")
    private String approvalNotes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    private LocalDateTime approvedAt;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}

