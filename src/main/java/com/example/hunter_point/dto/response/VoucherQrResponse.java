package com.example.hunter_point.dto.response;

import com.example.hunter_point.entity.enums.VoucherQrStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class VoucherQrResponse {
    private String code;
    private String qrImageBase64;
    private LocalDateTime expiryDate;
    private VoucherQrStatus status;
}
