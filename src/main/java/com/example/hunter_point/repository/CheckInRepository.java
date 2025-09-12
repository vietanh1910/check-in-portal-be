package com.example.hunter_point.repository;

import com.example.hunter_point.dto.response.CampaignPointResponse;
import com.example.hunter_point.entity.CheckIn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckInRepository extends JpaRepository<CheckIn, Long> {

    Page<CheckIn> findByCampaignId(Long campaignId, Pageable pageable);

    Page<CheckIn> findByUserId(Long userId, Pageable pageable);

    @Query("""
        SELECT FUNCTION('DATE', ci.checkInTime), COUNT(ci)
        FROM CheckIn ci
        JOIN ci.campaign camp
        WHERE camp.allocator.id = :allocatorId
        GROUP BY FUNCTION('DATE', ci.checkInTime)
        ORDER BY FUNCTION('DATE', ci.checkInTime)
    """)
    List<Object[]> findDailyCheckinsByAllocator(@Param("allocatorId") Long allocatorId);

    @Query("""
        SELECT new com.example.hunter_point.dto.response.CampaignPointResponse(
            camp.name,
            SUM(ci.pointsEarned)
        )
        FROM CheckIn ci
        JOIN ci.campaign camp
        WHERE camp.allocator.id = :allocatorId
        GROUP BY camp.name
        ORDER BY camp.name
    """)
    List<CampaignPointResponse> findCampaignPointsByAllocator(@Param("allocatorId") Long allocatorId);
}

