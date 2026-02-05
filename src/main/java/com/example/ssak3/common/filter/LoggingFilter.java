package com.example.ssak3.common.filter;

import com.example.ssak3.common.model.AuthUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * HTTP 요청에 대한 로그 담당
 */
@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 실행 시작 시간
        long startTime = System.currentTimeMillis();

        // API 요청 정보
        String url = request.getRequestURI();
        String method = request.getMethod();
        String queryParams = request.getQueryString();

        // 인증 정보 꺼내기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;

        if (auth != null && auth.isAuthenticated()) {
            AuthUser user = (AuthUser) auth.getPrincipal();
            userId = user.getId();
        }

        // TraceId 생성
        String traceId = UUID.randomUUID().toString().substring(0, 8);
        
        // MDC에 저장: 이 스레드에서 찍히는 모든 로그에 traceId가 붙음
        MDC.put("traceId", traceId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            int statusCode = response.getStatus();

            long endTime = System.currentTimeMillis();

            if (statusCode >= 500) {
                log.error("USER_ID: {} | STATUS_CODE: {} | URL: {} | METHOD: {} | QUERY_PARAMS: {} | EXECUTION_TIME: {} ms",
                        userId, statusCode, url, method, queryParams, endTime - startTime);
            } else if (statusCode >= 400) {
                log.warn("USER_ID: {} | STATUS_CODE: {} | URL: {} | METHOD: {} | QUERY_PARAMS: {} | EXECUTION_TIME: {} ms",
                        userId, statusCode, url, method, queryParams, endTime - startTime);
            }

            // 스레드 풀을 재사용하기 때문에 작업이 끝나면 MDC를 반드시 비워야 함
            MDC.clear();
        }
    }
}
