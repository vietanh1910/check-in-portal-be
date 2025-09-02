package com.example.hunter_point.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // Dùng 409 Conflict vì request hợp lệ nhưng tài nguyên đã hết
public class VoucherOutOfStockException extends RuntimeException {
    public VoucherOutOfStockException(String message) {
        super(message);
    }
}