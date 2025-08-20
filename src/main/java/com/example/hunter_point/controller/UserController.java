package com.example.hunter_point.controller;

import com.example.hunter_point.dto.response.UserResponse;
import com.example.hunter_point.entity.enums.ERole;
import com.example.hunter_point.entity.enums.UserStatus;
import com.example.hunter_point.service.UserService;
import com.example.hunter_point.utils.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // List users with search + filter + pagination
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ListResponse<UserResponse> searchUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) ERole role,
            @RequestParam(required = false) UserStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.searchUsers(keyword, role, status, pageable);
    }

    // Get user detail
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public GetDetailResponse<UserResponse> getUser(@PathVariable Long id) {
        try {
            return userService.getUserById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return GenerateResponse.generateErrorGetDetailResponse();
        }
    }

    // Update user
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public SimpleResponse updateUser(@PathVariable Long id, @RequestBody UserResponse updateDto) {
        try {
            return userService.updateUser(id, updateDto);
        } catch (Exception e) {
            e.printStackTrace();
            return GenerateResponse.generateErrorSimpleResponse("BAD_REQUEST");
        }
    }

    // Approve user
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/approve")
    public SimpleResponse approveUser(@PathVariable Long id) {
        try {
            return userService.approveUser(id);
        } catch (Exception e) {
            e.printStackTrace();
            return GenerateResponse.generateSuccessSimpleResponse();
        }
    }

    // Block user
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/block")
    public SimpleResponse blockUser(@PathVariable Long id) {
        userService.blockUser(id);
        try {
            return userService.blockUser(id);
        } catch (Exception e) {
            e.printStackTrace();
            return GenerateResponse.generateSuccessSimpleResponse();
        }
    }
}
