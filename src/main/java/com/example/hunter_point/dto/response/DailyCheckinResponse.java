package com.example.hunter_point.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DailyCheckinResponse {
    private String date;   // yyyy-MM-dd
    private Long checkin;  // tổng số check-in trong ngày
}
