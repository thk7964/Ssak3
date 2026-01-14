package com.example.ssak3.domain.auth.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.domain.auth.model.request.LoginRequest;
import com.example.ssak3.domain.auth.model.request.SignupRequest;
import com.example.ssak3.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    /**
     * 회원 가입 API
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signupApi(@Valid @RequestBody SignupRequest request) {

        ApiResponse response = ApiResponse.success("회원 가입에 성공했습니다.", authService.signup(request));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 로그인 API
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> loginApi(@RequestBody LoginRequest request) {

        ApiResponse response = ApiResponse.success("로그인에 성공했습니다.", authService.login(request));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
