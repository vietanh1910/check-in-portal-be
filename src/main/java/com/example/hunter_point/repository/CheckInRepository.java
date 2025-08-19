package com.example.hunter_point.repository;

import com.example.hunter_point.entity.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckInRepository extends JpaRepository<CheckIn, Long> {

    List<CheckIn> findByCampaignId(Long campaignId);

    List<CheckIn> findByUserId(Long userId);
}

