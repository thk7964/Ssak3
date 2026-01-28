package com.example.ssak3.domain.auth.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.domain.auth.service.OAuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ssak3/auth/login/kakao")
public class OAuthController {

    private final OAuthService oAuthService;

    /**
     * 프론트가 요청하면 카카오 로그인 URL 제공
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getKakaoAuthUrl() {

        ApiResponse response = ApiResponse.success("카카오 로그인 URL을 응답했습니다.", oAuthService.getKakaoAuthUrl());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * callback 될 url 경로 (인가 코드 발급)
     */
    @GetMapping("/callback")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse httpServletResponse) {

        boolean isNewUser = oAuthService.kakaoLogin(code, httpServletResponse);

        if (isNewUser) {
            return "redirect:/additional-info.html";
        }

        return "redirect:/home.html";
    }

}

