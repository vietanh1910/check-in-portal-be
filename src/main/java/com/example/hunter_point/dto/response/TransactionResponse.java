package com.example.hunter_point.dto.response;

import com.example.hunter_point.entity.enums.TransactionStatus;
import com.example.hunter_point.entity.enums.TransactionType;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {
    private Long id;
    private TransactionType type;
    private Integer point;
    private Double amount;
    private String description;
    private Instant createdAt;
    private Long userId;
    private TransactionStatus status;
}
