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

    // Quan hệ với Allocator (User)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "allocator_id", nullable = false)
    private User allocator;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String locationName;

    @Column(precision = 10, scale = 8, nullable = false)
    private BigDecimal latitude;

    @Column(precision = 11, scale = 8, nullable = false)
    private BigDecimal longitude;

    private Integer radiusMeters;

    private String requiredWifiSsid;

    private String requiredWifiBssid;

    @Column(nullable = false)
    private Integer pointsPerCheckin;

    private Integer maxCheckinsPerUser;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal totalBudget;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal remainingBudget;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
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
