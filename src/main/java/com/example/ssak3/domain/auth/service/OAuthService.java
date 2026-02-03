package com.example.ssak3.domain.auth.service;

import com.example.ssak3.common.enums.OAuthProvider;
import com.example.ssak3.common.utils.JwtUtil;
import com.example.ssak3.domain.auth.client.KakaoClient;
import com.example.ssak3.domain.auth.model.response.KakaoAuthUrlResponse;
import com.example.ssak3.domain.auth.model.response.KakaoUserInfoResponse;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final KakaoClient kakaoClient;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public KakaoAuthUrlResponse getKakaoAuthUrl() {
        return kakaoClient.getAuthUrl();
    }

    @Transactional
    public boolean kakaoLogin(String code, HttpServletResponse httpServletResponse) {

        boolean isNewUser = false; // 카카오 로그인 시 기존 회원인지 확인하는 변수

        // 1. 받은 인가 코드를 기반으로 Access Token 얻기
        String accessToken = kakaoClient.getKakaoToken(code).getAccessToken();

        // 2. Access Token을 통해 사용자 정보 얻기
        KakaoUserInfoResponse kakaoUserInfo = kakaoClient.getKakaoUserInfo(accessToken);

        // 3. 우리 서비스에 가입된 유저가 아니면 회원 가입
        User user = userRepository.findByEmail(kakaoUserInfo.getKakaoAccount().getEmail())
                .orElseGet(() -> signup(kakaoUserInfo));

        if (user.getPhone() == null) { // phone이 null이면 카카오 로그인으로 새로 가입된 유저
            isNewUser = true;
        }

        String rawToken = jwtUtil.createToken(user.getId(), user.getEmail(), user.getRole());
        String token = jwtUtil.substringToken(rawToken);

        Cookie cookie = new Cookie("accessToken", token);
        
        cookie.setPath("/");
        cookie.setHttpOnly(true); // XSS 방어 코드
        cookie.setMaxAge(60 * 60);

        httpServletResponse.addCookie(cookie); // 쿠키에 실어 보냄

        return isNewUser;
    }

    /**
     * 카카오에서 얻어온 정보로 회원가입
     */
    private User signup(KakaoUserInfoResponse kakaoUserInfo) {

        UUID uuid = UUID.randomUUID();

        User user = new User(
                null,
                kakaoUserInfo.getKakaoAccount().getProfile().getNickname(),
                kakaoUserInfo.getKakaoAccount().getEmail(),
                uuid.toString(),
                null,
                null,
                null
        );

        user.updateProvider(OAuthProvider.KAKAO);

        return userRepository.save(user);
    }
}
