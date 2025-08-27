package com.example.hunter_point.service.impl;

import com.example.hunter_point.dto.response.UserResponse;
import com.example.hunter_point.entity.User;
import com.example.hunter_point.entity.enums.ERole;
import com.example.hunter_point.entity.enums.UserStatus;
import com.example.hunter_point.repository.UserRepository;
import com.example.hunter_point.service.UserService;
import com.example.hunter_point.utils.response.*;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private UserResponse mapToDTO(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .address(user.getAddress())
                .phone(user.getPhone())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole())
                .status(user.getStatus())
                .emailVerified(user.isEmailVerified())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .lastLogin(user.getLastLogin())
                .build();
    }

    @Override
    public ListResponse<UserResponse> searchUsers(String keyword, ERole role, UserStatus status, Pageable pageable) {
        Page<User> pageResult = userRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.isBlank()) {
                String likeValue = "%" + keyword.toLowerCase() + "%";
                Predicate byName = cb.like(cb.lower(root.get("fullName")), likeValue);
                Predicate byEmail = cb.like(cb.lower(root.get("email")), likeValue);
                predicates.add(cb.or(byName, byEmail));
            }
            if (role != null) {
                predicates.add(cb.equal(root.get("role"), role));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }, pageable);

        List<UserResponse> responseDTOS = pageResult.getContent()
                .stream()
                .map(this::mapToDTO)
                .toList();

        return GenerateResponse.generateSuccessListResponse(
                responseDTOS,
                pageResult.getTotalElements()
        );
    }

    @Override
    public GetDetailResponse<UserResponse> getUserById(Long id) {
        if (id == null) {
            return GenerateResponse.generateErrorGetDetailResponse("User ID cannot be null");
        }
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return GenerateResponse.generateErrorGetDetailResponse("User not found");
        }
        User user = userOptional.get();
        UserResponse userResponse = mapToDTO(user);
        return GenerateResponse.generateSuccessGetDetailResponse(userResponse);
    }

    @Override
    public SimpleResponse updateUser(Long id, UserResponse updateDto) {
        if (id == null) {
            return GenerateResponse.generateErrorSimpleResponse("User ID cannot be null");
        }
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return GenerateResponse.generateErrorSimpleResponse("User not found");
        }
        User user = userOptional.get();
        user.setFullName(updateDto.getFullName());
        user.setPhone(updateDto.getPhone());
        user.setAvatarUrl(updateDto.getAvatarUrl());
        user.setRole(updateDto.getRole());
        user.setStatus(updateDto.getStatus());
        userRepository.save(user);
        return GenerateResponse.generateSuccessSimpleResponse();
    }

    @Override
    public SimpleResponse approveUser(Long id) {
        if (id == null) {
            return GenerateResponse.generateErrorSimpleResponse("User ID cannot be null");
        }
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return GenerateResponse.generateErrorSimpleResponse("User not found");
        }
        User user = userOptional.get();
        user.setStatus(UserStatus.ACTIVE);
        user.setEmailVerified(true);
        userRepository.save(user);
        return GenerateResponse.generateSuccessSimpleResponse();
    }

    @Override
    public SimpleResponse blockUser(Long id) {
        if (id == null) {
            return GenerateResponse.generateErrorSimpleResponse("User ID cannot be null");
        }
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return GenerateResponse.generateErrorSimpleResponse("User not found");
        }
        User user = userOptional.get();
        user.setStatus(UserStatus.BANNED);
        userRepository.save(user);
        return GenerateResponse.generateSuccessSimpleResponse();
    }
}
