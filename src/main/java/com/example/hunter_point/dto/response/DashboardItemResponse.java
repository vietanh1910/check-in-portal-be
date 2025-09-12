package com.example.hunter_point.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardItemResponse {
    private String title;
    private Object value;
    private String desc;
}
