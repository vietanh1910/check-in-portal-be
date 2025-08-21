package com.example.hunter_point.dto.request;

import lombok.Data;

@Data
public class CheckinRequest {
    private Long userId;
    private Long campaignId;
    private Integer points;
    private String verify = "Verified";
}
