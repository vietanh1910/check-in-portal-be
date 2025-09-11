package com.example.hunter_point.controller;

// Bạn đã import đúng hết các class cần thiết, giữ nguyên
import com.example.hunter_point.dto.request.VoucherRequest;
import com.example.hunter_point.dto.response.VoucherResponse;
import com.example.hunter_point.exception.InsufficientPointsException;
import com.example.hunter_point.exception.ResourceNotFoundException;
import com.example.hunter_point.exception.VoucherOutOfStockException;
import com.example.hunter_point.security.UserDetailsImpl;
import com.example.hunter_point.service.VoucherService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vouchers")
@RequiredArgsConstructor
@CrossOrigin // Thêm annotation này để cho phép gọi từ Flutter/Postman khi test ở local
public class VoucherController {

    private final VoucherService voucherService;

    @GetMapping()
    public ResponseEntity<List<VoucherResponse>> getAll() {
        return ResponseEntity.ok(voucherService.getAll());
    }

    // --- CÁC ENDPOINT CRUD CỦA BẠN (Đã rất tốt, giữ nguyên) ---
    @GetMapping("/get-all-for-user")
    public ResponseEntity<List<VoucherResponse>> getAllAvailableForUser(Authentication authentication) {
        // Lấy userId từ token... (Logic này bạn cần tự hoàn thiện)
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        // Gọi một method mới trong service để lấy voucher có thể đổi
        return ResponseEntity.ok(voucherService.getAvailableVouchersForUser(userDetails.getId()));
    }
    // ... các endpoint GET, POST, PUT, DELETE khác giữ nguyên ...
    @GetMapping("/{id}")
    public ResponseEntity<VoucherResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(voucherService.getById(id));
    }

    @PostMapping
    public ResponseEntity<VoucherResponse> create(@RequestBody VoucherRequest request) {
        return ResponseEntity.ok(voucherService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VoucherResponse> update(@PathVariable Long id, @RequestBody VoucherRequest request) {
        return ResponseEntity.ok(voucherService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        voucherService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // --- ENDPOINT CHÍNH (Đã rất tốt, giữ nguyên) ---

    @PostMapping("/{id}/redeem")
    public ResponseEntity<?> redeemVoucher(@PathVariable Long id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal().toString())) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized: User is not authenticated."));
        }
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Long currentUserId = userDetails.getId();
            voucherService.redeem(id, currentUserId);
            return ResponseEntity.ok(Map.of("message", "Đổi voucher thành công!"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (InsufficientPointsException | VoucherOutOfStockException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "An internal server error occurred: " + e.getMessage()));
        }
    }

    // --- ENDPOINT TEST (Đã được hoàn thiện) ---

    @PostMapping("/redeem-legacy-test")
    public ResponseEntity<String> redeemVoucherLegacy(@RequestBody RedeemRequest req) {
        if (req == null || req.getVoucherId() == null) {
            return ResponseEntity.badRequest().body("voucherId is required in the request body.");
        }

        // Luôn dùng userId = 1L để test nhanh mà không cần đăng nhập
        Long voucherId = req.getVoucherId();
        Long testUserId = 1L;

        try {
            // Gọi đến service với user ID test
            voucherService.redeem(voucherId, testUserId);
            return ResponseEntity.ok("TEST THÀNH CÔNG: Voucher đã được đổi cho user test (ID=1)!");
        } catch (Exception e) {
            // Trả về lỗi nếu service ném ra exception
            return ResponseEntity.badRequest().body("TEST THẤT BẠI: " + e.getMessage());
        }
    }

    // Lớp DTO tĩnh cho request body của endpoint test
    private static class RedeemRequest {
        private Long voucherId;
        public Long getVoucherId() { return voucherId; }
        public void setVoucherId(Long voucherId) { this.voucherId = voucherId; }
    }
}