package com.example.hunter_point.repository;

import com.example.hunter_point.entity.CheckIn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckInRepository extends JpaRepository<CheckIn, Long> {

    Page<CheckIn> findByCampaignId(Long campaignId, Pageable pageable);

    Page<CheckIn> findByUserId(Long userId, Pageable pageable);
}

