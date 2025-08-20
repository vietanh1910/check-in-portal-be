package com.example.hunter_point.service;

import com.example.hunter_point.dto.response.UserResponse;
import com.example.hunter_point.entity.enums.ERole;
import com.example.hunter_point.entity.enums.UserStatus;
import com.example.hunter_point.utils.response.DetailResponse;
import com.example.hunter_point.utils.response.GetDetailResponse;
import com.example.hunter_point.utils.response.ListResponse;
import com.example.hunter_point.utils.response.SimpleResponse;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    ListResponse<UserResponse> searchUsers(String keyword, ERole role, UserStatus status, Pageable pageable);
    GetDetailResponse<UserResponse> getUserById(Long id);
    SimpleResponse updateUser(Long id, UserResponse updateDto);
    SimpleResponse approveUser(Long id);
    SimpleResponse blockUser(Long id);
}
