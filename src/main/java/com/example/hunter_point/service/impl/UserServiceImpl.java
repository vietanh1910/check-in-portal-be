package com.example.hunter_point.service.impl;

import com.example.hunter_point.dto.response.UserResponse;
import com.example.hunter_point.entity.User;
import com.example.hunter_point.entity.enums.ERole;
import com.example.hunter_point.entity.enums.UserStatus;
import com.example.hunter_point.repository.UserRepository;
import com.example.hunter_point.service.UserService;
import com.example.hunter_point.utils.response.GenerateResponse;
import com.example.hunter_point.utils.response.ListResponse;
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

        // map entity -> DTO
        List<UserResponse> responseDTOS = pageResult.getContent()
                .stream()
                .map(this::mapToDTO)
                .toList();

        // dùng helper giống mẫu
        return GenerateResponse.generateSuccessListResponse(
                responseDTOS,
                pageResult.getTotalElements()
        );
    }


    @Override
    public Optional<UserResponse> getUserById(Long id) {
        return userRepository.findById(id).map(this::mapToDTO);
    }

    @Override
    public UserResponse updateUser(Long id, UserResponse updateDto) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existing.setFullName(updateDto.getFullName());
        existing.setPhone(updateDto.getPhone());
        existing.setAvatarUrl(updateDto.getAvatarUrl());
        existing.setRole(updateDto.getRole());
        existing.setStatus(updateDto.getStatus());

        return mapToDTO(userRepository.save(existing));
    }

    @Override
    public UserResponse approveUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(UserStatus.ACTIVE);
        user.setEmailVerified(true);
        return mapToDTO(userRepository.save(user));
    }

    @Override
    public void blockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(UserStatus.BANNED);
        userRepository.save(user);
    }
}
