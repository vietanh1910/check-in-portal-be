package com.example.hunter_point.controller;

import com.example.hunter_point.dto.response.CheckInResponse;
import com.example.hunter_point.service.CheckInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/check-ins")
@RequiredArgsConstructor
public class CheckInController {

    private final CheckInService checkInService;

    // Lấy tất cả check-in theo campaign
    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<List<CheckInResponse>> getByCampaign(@PathVariable Long campaignId) {
        return ResponseEntity.ok(checkInService.getCheckInsByCampaign(campaignId));
    }

    // Lấy tất cả check-in theo user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CheckInResponse>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(checkInService.getCheckInsByUser(userId));
    }

    // Tạo check-in mới
    @PostMapping
    public ResponseEntity<CheckInResponse> createCheckIn(
            @RequestParam Long userId,
            @RequestParam Long campaignId,
            @RequestParam Integer points,
            @RequestParam(defaultValue = "Verified") String verify
    ) {
        return ResponseEntity.ok(checkInService.createCheckIn(userId, campaignId, points, verify));
    }
}
