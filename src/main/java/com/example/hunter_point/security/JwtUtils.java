package com.example.hunter_point.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    // === CÁC BIẾN @Value ĐỌC TỪ application.properties ===

    // Giữ nguyên tên key cũ của bạn cho Login JWT
    @Value("${jwt.secret}")
    private String jwtLoginSecret;

    @Value("${jwt.expiration.ms}")
    private int jwtLoginExpirationMs;

    // Thêm các key mới cho QR JWT
    @Value("${jwt.qr.secret}")
    private String jwtQrSecret;

    @Value("${jwt.qr.expiration.ms}")
    private long jwtQrExpirationMs;

    // --- LOGIC CHO LOGIN TOKEN (Giữ nguyên không đổi) ---

    private Key getLoginSigningKey() {
        return Keys.hmacShaKeyFor(jwtLoginSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtLoginExpirationMs))
                .signWith(getLoginSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getLoginSigningKey()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getLoginSigningKey()).build().parse(authToken);
            return true;
        } catch (JwtException e) {
            logger.error("Invalid Login JWT: {}", e.getMessage());
            return false;
        }
    }

    // --- LOGIC MỚI CHO QR TOKEN ---

    private Key getQrSigningKey() {
        return Keys.hmacShaKeyFor(jwtLoginSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateQrToken(Long redemptionHistoryId, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(redemptionHistoryId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtQrExpirationMs))
                .signWith(getQrSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaimsFromQr(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getQrSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        try {
            return extractAllClaimsFromQr(token).getExpiration().before(new Date());
        } catch (JwtException e) {
            logger.error("Invalid QR Token: {}", e.getMessage());
            return true;
        }
    }

    public Long extractRedemptionIdFromQr(String token) {
        String subject = extractAllClaimsFromQr(token).getSubject();
        return Long.parseLong(subject);
    }
}