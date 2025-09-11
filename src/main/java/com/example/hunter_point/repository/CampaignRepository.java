package com.example.hunter_point.repository;

import com.example.hunter_point.entity.Campaign;
import com.example.hunter_point.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    List<Campaign> findByAllocatorId(Long allocatorId);

    List<Campaign> findByLatitudeAndLongitude(BigDecimal latitude, BigDecimal longitude);

    Page<Campaign> findByAllocatorIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    @Query(value = """
        SELECT c.*,
               (6371 * acos(
                   cos(radians(:lat)) * cos(radians(c.latitude)) *
                   cos(radians(c.longitude) - radians(:lon)) +
                   sin(radians(:lat)) * sin(radians(c.latitude))
               )) AS distance
        FROM campaigns c
        WHERE c.status = 'APPROVED'
          AND NOT EXISTS (
              SELECT 1 FROM check_ins ch
              WHERE ch.campaign_id = c.id
                AND ch.user_id = :userId
          )
        ORDER BY distance DESC
        """,
            countQuery = """
        SELECT COUNT(*)
        FROM campaigns c
        WHERE c.status = 'APPROVED'
          AND NOT EXISTS (
              SELECT 1 FROM check_ins ch
              WHERE ch.campaign_id = c.id
                AND ch.user_id = :userId
          )
        """,
            nativeQuery = true)
    Page<Campaign> findCampaignsNotCheckedInByUser(
            @Param("lat") BigDecimal lat,
            @Param("lon") BigDecimal lon,
            @Param("userId") Long userId,
            Pageable pageable
    );

    Page<Campaign> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
