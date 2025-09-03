package com.example.hunter_point.repository;

import com.example.hunter_point.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

import java.time.LocalDateTime;

public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Voucher v " +
            "SET v.status = com.example.hunter_point.entity.enums.VoucherStatus.EXPIRED, " +
            "v.updatedAt = CURRENT_TIMESTAMP " +
            "WHERE v.endDate < :now AND v.status <> com.example.hunter_point.entity.enums.VoucherStatus.EXPIRED")
    int expireAll(LocalDateTime now);

    @Modifying
    @Transactional
    @Query("UPDATE Voucher v SET v.status = com.example.hunter_point.entity.enums.VoucherStatus.ACTIVE, " +
            "v.updatedAt = CURRENT_TIMESTAMP " +
            "WHERE v.startDate <= :now AND v.endDate >= :now AND v.status = com.example.hunter_point.entity.enums.VoucherStatus.INACTIVE")
    int activateAll(LocalDateTime now);
    @Query("SELECT v FROM Voucher v WHERE v.isPublished = true AND (v.quantity = 0 OR v.claimed < v.quantity) AND NOT EXISTS (" +
            "SELECT rh FROM RedemptionHistory rh WHERE rh.voucher.id = v.id AND rh.user.id = :userId" +
            ")")
    List<Voucher> findAvailableVouchersForUser(@Param("userId") Long userId);
}
