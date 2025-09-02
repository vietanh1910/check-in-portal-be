// File: com.example.hunter_point.controller.RedemptionHistoryController.java
package com.example.hunter_point.controller;

import com.example.hunter_point.dto.response.RedemptionHistoryResponse;
import com.example.hunter_point.service.RedemptionHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
@CrossOrigin
public class RedemptionHistoryController {

    private final RedemptionHistoryService historyService;

    @GetMapping("/my-vouchers/{userId}")
    public ResponseEntity<List<RedemptionHistoryResponse>> getMyVouchers(@PathVariable Long userId) {
        return ResponseEntity.ok(historyService.getHistoryForUser(userId));
    }
}