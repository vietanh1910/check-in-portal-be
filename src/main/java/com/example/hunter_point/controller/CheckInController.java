package com.example.hunter_point.controller;

import com.example.hunter_point.dto.request.CheckinRequest;
import com.example.hunter_point.dto.response.CheckInResponse;
import com.example.hunter_point.entity.User;
import com.example.hunter_point.repository.UserRepository;
import com.example.hunter_point.security.UserDetailsImpl;
import com.example.hunter_point.service.CheckInService;
import com.example.hunter_point.utils.response.GenerateResponse;
import com.example.hunter_point.utils.response.ListResponse;
import com.example.hunter_point.utils.response.SimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/check-ins")
@RequiredArgsConstructor
public class CheckInController {

    private final CheckInService checkInService;

    private final UserRepository userRepository;

    // Lấy tất cả check-in theo campaign
    @GetMapping("/campaign/{campaignId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ListResponse<CheckInResponse> getByCampaign(
            @PathVariable Long campaignId,
            @PathVariable int page,
            @PathVariable int size
            ) {
        try {
            return checkInService.getCheckInsByCampaign(campaignId, page, size);
        } catch (Exception e) {
            e.printStackTrace();
            return GenerateResponse.generateErrorListResponse("BAD_REQUEST");
        }
    }

    // Lấy tất cả check-in theo user
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ListResponse<CheckInResponse> getByUser(
            @PathVariable Long userId,
            @PathVariable int page,
            @PathVariable int size
    ) {
        try {
            return checkInService.getCheckInsByUser(userId, page, size);
        } catch (Exception e) {
            e.printStackTrace();
            return GenerateResponse.generateErrorListResponse("BAD_REQUEST");
        }
    }

    // Tạo check-in mới
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public SimpleResponse createCheckIn(@RequestBody CheckinRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> userOptional = userRepository.findById(userDetails.getId());
        if (userOptional.isEmpty()) {
            return GenerateResponse.generateErrorSimpleResponse("User not found");
        }
        try {
            return checkInService.createCheckIn(
                    userOptional.get().getId(),
                    request.getCampaignId(),
                    request.getPoints(),
                    request.getVerify()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return GenerateResponse.generateErrorSimpleResponse("BAD_REQUEST");
        }
    }
}
