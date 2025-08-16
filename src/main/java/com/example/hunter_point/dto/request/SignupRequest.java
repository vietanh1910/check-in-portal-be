package com.example.hunter_point.dto.request;

import com.example.hunter_point.entity.enums.ERole;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class SignupRequest {
    // Bắt buộc cho tất cả
    private String email;
    private String password;
    private ERole role; // Client phải gửi lên vai trò muốn đăng ký

    // Tùy chọn
    private String fullName;
    private String phone;
    private String avatarUrl;
    private String businessName;
    private String contactPerson;
    private String address;
    private String businessType;
    private String taxId;
}
