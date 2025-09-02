package com.example.hunter_point.repository;

import com.example.hunter_point.entity.VoucherQr;
import com.example.hunter_point.entity.enums.VoucherQrStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VoucherQrRepository extends JpaRepository<VoucherQr, Long> {

    Optional<VoucherQr> findByCode(String code);

    // Tìm QR ACTIVE mới nhất còn hạn cho 1 voucher + user
    Optional<VoucherQr> findTopByVoucher_IdAndUserIdAndStatusAndExpiryDateAfterOrderByIdDesc(
            Long voucherId,
            Long userId,
            VoucherQrStatus status,
            LocalDateTime now
    );
}
