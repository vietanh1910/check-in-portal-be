
package com.example.hunter_point.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler {

    // Bắt lỗi khi người dùng cố gắng đổi voucher đã đổi rồi
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalStateException(IllegalStateException ex, WebRequest request) {
        // Tạo một đối tượng JSON để trả về, ví dụ: {"error": "Bạn đã đổi voucher này rồi."}
        Map<String, String> body = Map.of("error", ex.getMessage());
        // Trả về mã lỗi 409 Conflict (xung đột) thay vì 500
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    // Bắt lỗi không đủ điểm
    @ExceptionHandler(InsufficientPointsException.class)
    public ResponseEntity<Object> handleInsufficientPointsException(InsufficientPointsException ex, WebRequest request) {
        Map<String, String> body = Map.of("error", ex.getMessage());
        // Trả về mã lỗi 400 Bad Request
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // Bắt lỗi voucher hết hàng
    @ExceptionHandler(VoucherOutOfStockException.class)
    public ResponseEntity<Object> handleVoucherOutOfStockException(VoucherOutOfStockException ex, WebRequest request) {
        Map<String, String> body = Map.of("error", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // Bắt các lỗi không tìm thấy tài nguyên (User, Voucher...)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        Map<String, String> body = Map.of("error", ex.getMessage());
        // Trả về mã lỗi 404 Not Found
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    // (Tùy chọn) Bắt tất cả các lỗi khác và trả về 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        Map<String, String> body = Map.of("error", "An unexpected internal server error occurred." + ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}