package com.example.hunter_point.entity;

import com.example.hunter_point.entity.enums.VoucherQrStatus;
import com.example.hunter_point.entity.enums.VoucherStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "voucher_qr")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherQr {
    @Enumerated(EnumType.STRING)
    private VoucherQrStatus status;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mỗi VoucherQr phải gắn với một Voucher gốc
    @ManyToOne
    @JoinColumn(name = "voucher_id", nullable = false)
    private Voucher voucher;

    private String code;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String qrImage; // base64 string QR code

    private LocalDateTime expiryDate; // hết hạn (vd: +3 phút sau khi tạo)

    private Long userId; // ai được phát voucher này
    @ManyToOne
    @JoinColumn(name = "redemption_history_id")
    private RedemptionHistory redemptionHistory;
}
