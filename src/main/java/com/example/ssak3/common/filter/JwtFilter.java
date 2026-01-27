package com.example.ssak3.common.filter;

import com.example.ssak3.common.enums.UserRole;
import com.example.ssak3.common.model.AuthUser;
import com.example.ssak3.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. 헤더에서 토큰 찾기 (일반 로그인)
        String accessToken = request.getHeader(JwtUtil.HEADER);

        // 2. 쿠키에서 토큰 찾기 (카카오 로그인)
        if (accessToken == null || !accessToken.startsWith(JwtUtil.BEARER_PREFIX)) {
            if (request.getCookies() != null) {
                accessToken = Arrays.stream(request.getCookies())
                        .filter(cookie -> "accessToken".equals(cookie.getName()))
                        .map(Cookie::getValue)
                        .findFirst()
                        .map(token -> {
                            if (!token.startsWith(JwtUtil.BEARER_PREFIX)) {
                                return JwtUtil.BEARER_PREFIX + token;
                            }
                            return token;
                        })
                        .orElse(null);
            }
        }

        if (accessToken == null || !accessToken.startsWith(JwtUtil.BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String rawToken = jwtUtil.substringToken(accessToken);
        Claims claims = jwtUtil.extractClaims(rawToken);

        Long id = Long.valueOf(claims.getSubject());
        String email = claims.get("email", String.class);
        String name = claims.get("name", String.class);
        UserRole role = UserRole.valueOf(claims.get("role", String.class));

        AuthUser authUser = new AuthUser(id, email, name, role);

        Authentication authentication
                = new UsernamePasswordAuthenticationToken(authUser, null, List.of(new SimpleGrantedAuthority("ROLE_" + role.name())));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
