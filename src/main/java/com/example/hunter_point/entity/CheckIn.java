package com.example.hunter_point.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "check_ins") // đặt tên table dạng snake_case
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quan hệ với User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Quan hệ với Campaign
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;

    // Thời gian check-in
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime checkInTime;

    // Điểm nhận được khi check-in
    @Column(nullable = false)
    private Integer pointsEarned;

    // Trạng thái xác minh (ví dụ: Verified / Pending)
    @Column(length = 50)
    private String verify;
}
