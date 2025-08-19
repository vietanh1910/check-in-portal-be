package com.example.hunter_point.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {
    private int statusCode;
    private String message;
    private T data;

    // Phân trang
    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;
}
