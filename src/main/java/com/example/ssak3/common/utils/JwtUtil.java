package com.example.ssak3.common.utils;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.UserRole;
import com.example.ssak3.common.exception.JwtAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

import static io.jsonwebtoken.Jwts.SIG.HS256;

@Slf4j
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

    public String createToken(Long id, String email, UserRole role) {
        return BEARER_PREFIX +
                Jwts.builder()
                        .subject(String.valueOf(id))
                        .claim("email", email)
                        .claim("role", role)
                        .issuedAt(new Date(System.currentTimeMillis()))
                        .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_TIME))
                        .signWith(key, HS256)
                        .compact();
    }

    public String substringToken(String token) throws JwtAuthenticationException {

        if (token == null || !token.startsWith(BEARER_PREFIX)) {
            throw new JwtAuthenticationException(ErrorCode.INVALID_TOKEN);
        }

        return token.substring(BEARER_PREFIX.length());
    }

    public Claims extractClaims(String token) {

        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            log.warn("message = 만료된 토큰 에러, class = {}", e.getClass());
            throw e;
        } catch (JwtException e) {
            log.warn("message = 유효하지 않은 토큰 에러, class = {}", e.getClass());
            throw new JwtAuthenticationException(ErrorCode.INVALID_TOKEN);
        }

    }

}
