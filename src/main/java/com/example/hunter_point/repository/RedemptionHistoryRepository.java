// File: com.example.hunter_point.repository.RedemptionHistoryRepository.java
package com.example.hunter_point.repository;

import com.example.hunter_point.entity.RedemptionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RedemptionHistoryRepository extends JpaRepository<RedemptionHistory, Long> {

    // Lấy tất cả lịch sử của một người dùng, sắp xếp theo ngày đổi mới nhất
    List<RedemptionHistory> findByUserIdOrderByRedeemedAtDesc(Long userId);

    // Tìm xem người dùng đã đổi voucher này hay chưa
    Optional<RedemptionHistory> findByUserIdAndVoucherId(Long userId, Long voucherId);

    // Đếm xem một người dùng đã đổi một voucher cụ thể bao nhiêu lần (hữu ích nếu sau này cho đổi nhiều lần)
    long countByUserIdAndVoucherId(Long userId, Long voucherId);
}