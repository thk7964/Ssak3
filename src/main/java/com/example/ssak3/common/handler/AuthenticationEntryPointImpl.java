package com.example.ssak3.common.handler;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.model.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        ErrorCode errorCode = (ErrorCode) request.getAttribute("exception");

        String message = (errorCode == null) ? "인증이 필요합니다." : errorCode.getMessage();

        ApiResponse errorResponse = ApiResponse.error(message);

        String result = objectMapper.writeValueAsString(errorResponse);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(result);
    }
}
