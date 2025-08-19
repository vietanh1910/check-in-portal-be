package com.example.hunter_point.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckInResponse {
    private Long id;
    private Long userId;
    private String userName;
    private Long campaignId;
    private LocalDateTime checkInTime;
    private Integer pointsEarned;
    private String verify;
}
