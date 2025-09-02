package com.example.hunter_point.controller;

import com.example.hunter_point.dto.request.VoucherQrConfirmRequest;
import com.example.hunter_point.dto.response.VoucherQrResponse;
import com.example.hunter_point.service.VoucherQrService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/voucher-qr")
@RequiredArgsConstructor
public class VoucherQrController {

    private final VoucherQrService voucherQrService;

    // FE (Flutter) bấm "Sử dụng": gọi lấy QR đang active hoặc tạo mới
    @PostMapping("/get-or-create")
    public ResponseEntity<VoucherQrResponse> getOrCreate(
            @RequestParam Long voucherId,
            @RequestParam Long userId
    ) {
        return ResponseEntity.ok(voucherQrService.getOrCreateActiveQr(voucherId, userId));
    }

    // App quét QR (hoặc BE hiển thị trang xác nhận) gọi validate để biết còn hạn không
    @GetMapping("/validate/{code}")
    public ResponseEntity<VoucherQrResponse> validate(@PathVariable String code) {
        return ResponseEntity.ok(voucherQrService.validate(code));
    }

    // Người dùng/chủ quầy chọn “Đồng ý” -> đánh dấu USED
    @PostMapping("/confirm")
    public ResponseEntity<Void> confirm(@RequestBody VoucherQrConfirmRequest req) {
        if (req.isAgree()) {
            voucherQrService.markUsed(req.getCode());
        }
        // nếu không đồng ý: không làm gì, để QR tự hết hạn sau 3 phút
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/status/{code}")
    public ResponseEntity<VoucherQrResponse> getStatus(@PathVariable String code) {
        // Chúng ta có thể dùng lại logic của hàm validate vì nó cũng trả về thông tin trạng thái
        return ResponseEntity.ok(voucherQrService.validate(code));
    }
}
