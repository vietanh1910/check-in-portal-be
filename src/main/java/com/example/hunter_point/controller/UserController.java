package com.example.hunter_point.controller;

import com.example.hunter_point.dto.response.UserResponse;
import com.example.hunter_point.entity.enums.ERole;
import com.example.hunter_point.entity.enums.UserStatus;
import com.example.hunter_point.service.UserService;
import com.example.hunter_point.utils.response.ListResponse;
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
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update user
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
                                                      @RequestBody UserResponse updateDto) {
        return ResponseEntity.ok(userService.updateUser(id, updateDto));
    }

    // Approve user
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/approve")
    public ResponseEntity<UserResponse> approveUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.approveUser(id));
    }

    // Block user
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/block")
    public ResponseEntity<Void> blockUser(@PathVariable Long id) {
        userService.blockUser(id);
        return ResponseEntity.noContent().build();
    }
}
