package com.example.hunter_point.dto.request;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class VoucherQrConfirmRequest {
    private String code;
    private boolean agree; // true = đồng ý, false = không
}
