package com.example.hunter_point.repository;

import com.example.hunter_point.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    List<Campaign> findByAllocatorId(Long allocatorId);

    List<Campaign> findByLatitudeAndLongitude(BigDecimal latitude, BigDecimal longitude);
}
