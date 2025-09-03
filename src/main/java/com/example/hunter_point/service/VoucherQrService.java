package com.example.hunter_point.service;

import com.example.hunter_point.dto.response.VoucherQrResponse;
import com.example.hunter_point.entity.Voucher;

public interface VoucherQrService {
    // Lấy QR đang ACTIVE (chưa hết hạn) nếu có, ngược lại tạo mới (timeout 3 phút)
    VoucherQrResponse getOrCreateActiveQr(Long voucherId, Long userId);

    // Chỉ validate khi quét (BE của quầy/nhân viên hoặc app khác)
    VoucherQrResponse validate(String code);

    // User bấm “Đồng ý” -> đánh dấu USED
    void markUsed(String code);
}
