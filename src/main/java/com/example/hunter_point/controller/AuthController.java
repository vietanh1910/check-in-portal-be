package com.example.hunter_point.controller;

import com.example.hunter_point.dto.response.JwtResponse;
import com.example.hunter_point.dto.request.LoginRequest;
import com.example.hunter_point.dto.response.MessageResponse;
import com.example.hunter_point.dto.request.SignupRequest;
import com.example.hunter_point.entity.User;
import com.example.hunter_point.entity.enums.UserStatus;
import com.example.hunter_point.repository.RoleRepository;
import com.example.hunter_point.repository.UserRepository;
import com.example.hunter_point.security.JwtUtils;
import com.example.hunter_point.security.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        User user = User.builder()
                .email(signUpRequest.getEmail())
                .password(encoder.encode(signUpRequest.getPassword()))
                .role(signUpRequest.getRole()) // Lấy vai trò chính từ request
                .status(UserStatus.ACTIVE) // Mặc định là active
                .emailVerified(false) // Mặc định là chưa xác thực
                .fullName(signUpRequest.getFullName())
                .phone(signUpRequest.getPhone())
                .avatarUrl(signUpRequest.getAvatarUrl())
                .businessName(signUpRequest.getBusinessName())
                .contactPerson(signUpRequest.getContactPerson())
                .address(signUpRequest.getAddress())
                .businessType(signUpRequest.getBusinessType())
                .taxId(signUpRequest.getTaxId())
                .build();


        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
