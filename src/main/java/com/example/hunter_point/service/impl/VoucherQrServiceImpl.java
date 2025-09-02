package com.example.hunter_point.service.impl;

import com.example.hunter_point.dto.response.VoucherQrResponse;
import com.example.hunter_point.entity.RedemptionHistory;
import com.example.hunter_point.entity.Voucher;
import com.example.hunter_point.entity.VoucherQr;
import com.example.hunter_point.entity.enums.RedemptionStatus;
import com.example.hunter_point.entity.enums.VoucherQrStatus;
import com.example.hunter_point.repository.RedemptionHistoryRepository;
import com.example.hunter_point.repository.VoucherQrRepository;
import com.example.hunter_point.repository.VoucherRepository;
import com.example.hunter_point.service.VoucherQrService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VoucherQrServiceImpl implements VoucherQrService {

    private final VoucherQrRepository voucherQrRepository;
    private final VoucherRepository voucherRepository;
    private final RedemptionHistoryRepository redemptionHistoryRepository;

    @Value("${app.base-url}")
    private String appBaseUrl;

    @Override
    @Transactional
    public VoucherQrResponse getOrCreateActiveQr(Long voucherId, Long userId) {
        Optional<VoucherQr> existingOptional = voucherQrRepository
                .findTopByVoucher_IdAndUserIdAndStatusAndExpiryDateAfterOrderByIdDesc(
                        voucherId, userId, VoucherQrStatus.ACTIVE, LocalDateTime.now());

        if (existingOptional.isPresent()) {
            VoucherQr existingQr = existingOptional.get();
            String validationUrl = appBaseUrl + "/validate.html?code=" + existingQr.getCode();
            existingQr.setQrImage(generateQrBase64(validationUrl));
            return toResponse(existingQr);
        }

        RedemptionHistory history = redemptionHistoryRepository.findByUserIdAndVoucherId(userId, voucherId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch sử đổi cho voucher ID: " + voucherId + " và user ID: " + userId));

        VoucherQr qr = new VoucherQr();
        qr.setVoucher(history.getVoucher());
        qr.setRedemptionHistory(history);
        qr.setUserId(userId);
        qr.setStatus(VoucherQrStatus.ACTIVE);
        qr.setExpiryDate(LocalDateTime.now().plusMinutes(3));

        String code = UUID.randomUUID().toString().replace("-", "");
        qr.setCode(code);

        String validationUrl = appBaseUrl + "/validate.html?code=" + code;
        qr.setQrImage(generateQrBase64(validationUrl));

        voucherQrRepository.save(qr);
        return toResponse(qr);
    }

    @Override
    @Transactional(readOnly = true)
    public VoucherQrResponse validate(String code) {
        VoucherQr qr = voucherQrRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("QR không tồn tại"));

        if (qr.getExpiryDate().isBefore(LocalDateTime.now())) {
            if (qr.getStatus() != VoucherQrStatus.EXPIRED) {
                qr.setStatus(VoucherQrStatus.EXPIRED);
                voucherQrRepository.save(qr);
            }
            return toResponse(qr);
        }

        return toResponse(qr);
    }

    @Override
    @Transactional
    public void markUsed(String code) {
        VoucherQr qr = voucherQrRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("QR không tồn tại"));

        if (qr.getExpiryDate().isBefore(LocalDateTime.now())) {
            qr.setStatus(VoucherQrStatus.EXPIRED);
            voucherQrRepository.save(qr);
            throw new RuntimeException("QR đã hết hạn");
        }
        if (qr.getStatus() == VoucherQrStatus.USED) {
            return;
        }

        qr.setStatus(VoucherQrStatus.USED);

        RedemptionHistory history = qr.getRedemptionHistory();
        if (history != null) {
            history.setStatus(RedemptionStatus.USED);
            redemptionHistoryRepository.save(history);
        }

        voucherQrRepository.save(qr);
    }

    private String generateQrBase64(String payload) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(payload, BarcodeFormat.QR_CODE, 256, 256);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", out);
            return Base64.getEncoder().encodeToString(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Không thể tạo QR code", e);
        }
    }

    private VoucherQrResponse toResponse(VoucherQr qr) {
        return VoucherQrResponse.builder()
                .code(qr.getCode())
                .qrImageBase64(qr.getQrImage())
                .expiryDate(qr.getExpiryDate())
                .status(qr.getStatus())
                .build();
    }
}