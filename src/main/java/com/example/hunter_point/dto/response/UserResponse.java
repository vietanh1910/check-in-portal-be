package com.example.hunter_point.dto.response;

import com.example.hunter_point.entity.enums.ERole;
import com.example.hunter_point.entity.enums.UserStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private String fullName;
    private String phone;
    private String avatarUrl;
    private ERole role;
    private UserStatus status;
    private boolean emailVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLogin;
}
