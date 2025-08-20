package com.example.hunter_point.dto.request;


import com.example.hunter_point.entity.enums.CampaignStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CampaignRequest {
    private String name;
    private String description;
    private String locationName;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Integer radiusMeters;
    private String requiredWifiSsid;
    private Integer pointsPerCheckin;
    private Integer maxCheckinsPerUser;
    private BigDecimal totalBudget;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private CampaignStatus status;
    private int page;
    private int size;
}
