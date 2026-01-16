package com.example.ssak3.common.utils;

import com.example.ssak3.common.enums.UserRole;
import com.example.ssak3.common.exception.JwtAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

import static io.jsonwebtoken.Jwts.SIG.HS256;

@Component
public class JwtUtil {

    public static final String HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L; // 60분

    @Value("${jwt.secret.key}")
    private String secretKey;

    private SecretKey key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(Long id, String email, String nickname, UserRole role) {
        return BEARER_PREFIX +
                Jwts.builder()
                        .subject(String.valueOf(id))
                        .claim("email", email)
                        .claim("nickname", nickname)
                        .claim("role", role)
                        .issuedAt(new Date(System.currentTimeMillis()))
                        .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_TIME))
                        .signWith(key, HS256)
                        .compact();
    }

    public boolean validateToken(String token) throws JwtAuthenticationException {

        if (token == null || token.isBlank())  {
            return false;
        }

        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            throw new JwtAuthenticationException("토큰 검증에 실패했습니다.");
        }
    }

    public String substringToken(String token) throws JwtAuthenticationException {

        if (token == null || !token.startsWith(BEARER_PREFIX)) {
            throw new JwtAuthenticationException("토큰이 유효하지 않습니다.");
        }

        return token.substring(BEARER_PREFIX.length());
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
