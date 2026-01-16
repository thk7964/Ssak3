package com.example.ssak3.common.filter;

import com.example.ssak3.common.enums.UserRole;
import com.example.ssak3.common.exception.JwtAuthenticationException;
import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.common.model.AuthUser;
import com.example.ssak3.common.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = request.getHeader(JwtUtil.HEADER);

        if (accessToken == null || !accessToken.startsWith(JwtUtil.BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String rawToken = jwtUtil.substringToken(accessToken);

            Claims claims = jwtUtil.extractClaims(rawToken);

            Long id = Long.valueOf(claims.getSubject());
            String email = claims.get("email", String.class);
            String nickname = claims.get("nickname", String.class);
            UserRole role = UserRole.valueOf(claims.get("role", String.class));

            AuthUser authUser = new AuthUser(id, email, nickname, role);

            Authentication authentication
                    = new UsernamePasswordAuthenticationToken(authUser, null, List.of(new SimpleGrantedAuthority("ROLE_" + role.name())));

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JwtAuthenticationException e) {
            authenticationEntryPoint.commence(request, response, e);
            return;
        } catch (ExpiredJwtException e) {
            ApiResponse errorResponse = ApiResponse.error("만료된 토큰입니다.");

            String result = objectMapper.writeValueAsString(errorResponse);

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().write(result);

            return;
        }

        filterChain.doFilter(request, response);
    }
}
