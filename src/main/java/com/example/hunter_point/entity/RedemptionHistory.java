package com.example.hunter_point.entity;
import com.example.hunter_point.entity.enums.RedemptionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "redemption_history")
public class RedemptionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // LIÊN KẾT TỚI ENTITY 'User' MÀ BẠN ĐÃ CÓ
    @ManyToOne(fetch = FetchType.LAZY) // Dùng LAZY để tối ưu hiệu suất
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // LIÊN KẾT TỚI ENTITY 'Voucher' MÀ BẠN ĐÃ CÓ
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_id", nullable = false)
    private Voucher voucher;

    @Column(nullable = false)
    private LocalDateTime redeemedAt; // Thời gian đổi

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RedemptionStatus status; // Trạng thái: NOT_USED hoặc USED

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    public LocalDateTime getRedeemedAt() {
        return redeemedAt;
    }

    public void setRedeemedAt(LocalDateTime redeemedAt) {
        this.redeemedAt = redeemedAt;
    }

    public RedemptionStatus getStatus() {
        return status;
    }

    public void setStatus(RedemptionStatus status) {
        this.status = status;
    }
}