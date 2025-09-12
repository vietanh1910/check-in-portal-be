package com.example.hunter_point.repository;

import com.example.hunter_point.dto.response.MonthlyRevenueResponse;
import com.example.hunter_point.dto.response.TopAllocatorResponse;
import com.example.hunter_point.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByUserId(Long userId, Pageable pageable);

    Optional<Transaction> findByCampaignId(Long id);

    // Lấy tất cả, order by created_at desc
    Page<Transaction> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // Lấy theo userId, order by created_at desc
    Page<Transaction> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    @Query("""
        SELECT new com.example.hunter_point.dto.response.TopAllocatorResponse(m.fullName, SUM(t.amount))
        FROM Transaction t
        JOIN User m ON t.userId = m.id AND m.role = 'ALLOCATOR'
        WHERE t.type = 'TOPUP'
          AND t.status = 'COMPLETED'
        GROUP BY m.fullName
        ORDER BY SUM(t.amount) DESC
    """)
    List<TopAllocatorResponse> findTopMerchants();

    @Query("""
        SELECT FUNCTION('MONTH', t.createdAt), SUM(t.amount)
        FROM Transaction t
        WHERE t.type = 'TOPUP'
          AND t.status = 'COMPLETED'
          AND YEAR(t.createdAt) = YEAR(CURRENT_DATE)
        GROUP BY FUNCTION('MONTH', t.createdAt)
        ORDER BY FUNCTION('MONTH', t.createdAt)
    """)
    List<Object[]> getMonthlyRevenueThisYear();

    @Query("""
        SELECT DAY(t.createdAt), SUM(t.amount)
        FROM Transaction t
        WHERE t.type = 'TOPUP'
          AND t.status = 'COMPLETED'
          AND MONTH(t.createdAt) = MONTH(CURRENT_DATE)
          AND YEAR(t.createdAt) = YEAR(CURRENT_DATE)
        GROUP BY DAY(t.createdAt)
        ORDER BY DAY(t.createdAt)
    """)
    List<Object[]> getDailyRevenueThisMonth();
}

