package com.example.hunter_point.entity;

import com.example.hunter_point.entity.enums.ERole;
import com.example.hunter_point.entity.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ERole role;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private boolean emailVerified;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private String fullName;

    private String phone;

    private String avatarUrl;

    private String businessName;
    private String contactPerson;
    @Column(columnDefinition = "TEXT")
    private String address;
    private String businessType;
    private String taxId;

    @Column(columnDefinition = "json")
    private String permissions; // Lưu dưới dạng chuỗi JSON

    private LocalDateTime lastLogin;
}
