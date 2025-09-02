package com.example.hunter_point.controller;

import com.example.hunter_point.dto.request.LoginRequest;
import com.example.hunter_point.dto.request.SignupRequest;
import com.example.hunter_point.dto.response.JwtResponse;
import com.example.hunter_point.dto.response.MessageResponse;
import com.example.hunter_point.entity.User;
import com.example.hunter_point.entity.enums.UserStatus;
import com.example.hunter_point.repository.UserRepository;
import com.example.hunter_point.security.JwtUtils;
import com.example.hunter_point.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        // Bước xác thực người dùng (giữ nguyên)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // ==========================================================
        // === LẤY SỐ XU TỪ userDetails VÀ TRUYỀN VÀO JwtResponse ===
        // ==========================================================
        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles,
                userDetails.getPoints() // <-- TRUYỀN SỐ XU VÀO ĐÂY
        ));
    }

    // Endpoint đăng ký (giữ nguyên)
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Lỗi: Email này đã được sử dụng!"));
        }

        User user = User.builder()
                .email(signUpRequest.getEmail())
                .password(encoder.encode(signUpRequest.getPassword()))
                .role(signUpRequest.getRole())
                .status(UserStatus.ACTIVE)
                .emailVerified(false)
                .fullName(signUpRequest.getFullName())
                .phone(signUpRequest.getPhone())
                .avatarUrl(signUpRequest.getAvatarUrl())
                .businessName(signUpRequest.getBusinessName())
                .contactPerson(signUpRequest.getContactPerson())
                .address(signUpRequest.getAddress())
                .businessType(signUpRequest.getBusinessType())
                .taxId(signUpRequest.getTaxId())
                .points(0) // Khởi tạo điểm cho user mới là 0
                .build();

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Đăng ký người dùng thành công!"));
    }
}