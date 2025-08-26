package com.example.hunter_point.entity;

import com.example.hunter_point.entity.enums.TransactionStatus;
import com.example.hunter_point.entity.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private Integer point;
    private Double amount;
    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
    private Long approvedBy;
    private Long userId;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
}


