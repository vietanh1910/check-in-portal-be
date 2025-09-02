package com.example.hunter_point.config;

import com.example.hunter_point.security.AuthEntryPointJwt;
import com.example.hunter_point.security.AuthTokenFilter;
import com.example.hunter_point.security.UserDetailsServiceImpl; // Đổi lại tên class
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor // Sử dụng Lombok để inject dependencies
public class WebSecurityConfig {

    private final UserDetailsServiceImpl userDetailsService; // Đã đổi tên class
    private final AuthEntryPointJwt unauthorizedHandler;
    private final AuthTokenFilter authTokenFilter;

    // Bean để mã hóa mật khẩu, không đổi
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean cung cấp UserDetailsService và PasswordEncoder cho Spring Security
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // Bean để quản lý quá trình xác thực trong Controller
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // (Mới) Bean cấu hình CORS để cho phép app Flutter gọi API
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080", "http://localhost:3000", "http://localhost:4200")); // Thêm các nguồn gốc bạn muốn cho phép
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    // ==========================================================
    // === ĐÂY LÀ PHẦN CẤU HÌNH BẢO MẬT CHÍNH ===
    // ==========================================================
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Áp dụng cấu hình CORS
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        http.csrf(csrf -> csrf.disable()) // Tắt CSRF
                // Xử lý lỗi khi truy cập trái phép
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                // Không tạo session, dùng JWT (stateless)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Phân quyền cho các request HTTP
                .authorizeHttpRequests(auth ->
                        auth
                                .anyRequest().permitAll()
                );

        // Cần thiết để H2 Console hoạt động trong iframe
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()));

        // Đăng ký provider xác thực
        http.authenticationProvider(authenticationProvider());

        // Thêm bộ lọc JWT của chúng ta vào trước bộ lọc mặc định của Spring
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}