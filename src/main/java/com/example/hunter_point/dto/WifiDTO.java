package com.example.hunter_point.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WifiDTO {
    private String ssid;
    private String bssid;
}
