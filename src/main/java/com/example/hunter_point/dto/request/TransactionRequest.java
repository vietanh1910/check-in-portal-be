package com.example.hunter_point.dto.request;

import com.example.hunter_point.entity.enums.TransactionStatus;
import com.example.hunter_point.entity.enums.TransactionType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequest {
    private TransactionType type;
    private Integer point;
    private Double amount;
    private String description;
    private Long userId;
}

