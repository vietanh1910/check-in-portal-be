package com.example.hunter_point.scheduler;

import com.example.hunter_point.entity.Voucher;
import com.example.hunter_point.entity.enums.VoucherStatus;
import com.example.hunter_point.repository.VoucherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class VoucherScheduler {

    private final VoucherRepository voucherRepository;

    /**
     * Cron: chạy mỗi ngày lúc 0h00
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void processVouchers() {
        LocalDateTime now = LocalDateTime.now();

        int expiredCount = voucherRepository.expireAll(now);
        int activatedCount = voucherRepository.activateAll(now);

        log.info("Cronjob: {} voucher hết hạn -> EXPIRED, {} voucher tới hạn -> ACTIVE", expiredCount, activatedCount);
    }
}


