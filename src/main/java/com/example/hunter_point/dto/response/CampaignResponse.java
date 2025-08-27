package com.example.hunter_point.dto.response;


import com.example.hunter_point.dto.LocationDTO;
import com.example.hunter_point.dto.WifiDTO;
import com.example.hunter_point.entity.enums.CampaignStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder // Dùng builder để dễ dàng map dữ liệu
public class CampaignResponse {
    private Long id;
    private String name;
    private String description;
    private String startDate; // FE cần string
    private String endDate;   // FE cần string
    private String startTime; // FE cần string
    private String endTime;   // FE cần string
    private String locationName;   // FE cần string
    private LocationDTO location;
    private Integer rewardPerCheckin;
    private Integer pointBudget;
    private WifiDTO wifi;
    private String qrUrl; // Sẽ sinh ra sau
    private long used; // Cần tính toán
    private long checkIns; // Cần tính toán
    private CampaignStatus status;
    private String createdAt; // FE cần string
    private String updatedAt; // FE cần string
    private Integer radiusMeters;
}
