package com.example.hunter_point.repository;

import com.example.hunter_point.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByUserId(Long userId, Pageable pageable);

    Optional<Transaction> findByCampaignId(Long id);

    // Lấy tất cả, order by created_at desc
    Page<Transaction> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // Lấy theo userId, order by created_at desc
    Page<Transaction> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}

