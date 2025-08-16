package com.example.hunter_point.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class LocationDTO {
    private BigDecimal lat;
    private BigDecimal lng;
}
