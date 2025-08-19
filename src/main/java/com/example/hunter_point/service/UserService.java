package com.example.hunter_point.service;

import com.example.hunter_point.dto.response.UserResponse;
import com.example.hunter_point.entity.enums.ERole;
import com.example.hunter_point.entity.enums.UserStatus;
import com.example.hunter_point.utils.response.ListResponse;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    ListResponse<UserResponse> searchUsers(String keyword, ERole role, UserStatus status, Pageable pageable);
    Optional<UserResponse> getUserById(Long id);
    UserResponse updateUser(Long id, UserResponse updateDto);
    UserResponse approveUser(Long id);
    void blockUser(Long id);
}
