package com.example.ssak3.domain.auth.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.common.utils.JwtUtil;
import com.example.ssak3.domain.auth.model.KakaoUserInfo;
import com.example.ssak3.domain.auth.model.response.KakaoAuthUrlResponse;
import com.example.ssak3.domain.auth.model.response.KakaoLoginResponse;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OAuthService {

    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.redirect_uri}")
    private String redirectUri;

    @Value("${kakao.client_secret}")
    private String clientSecret;

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public KakaoAuthUrlResponse getKakaoAuthUrl() {

        String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code";

        return KakaoAuthUrlResponse.from(kakaoAuthUrl);
    }

    @Transactional
    public boolean kakaoLogin(String code, HttpServletResponse httpServletResponse) {

        boolean isNewUser = false;

        // 1. 받은 인가 코드를 기반으로 Access Token 얻기
        String accessToken = getKakaoToken(code).getAccessToken();

        // 2. Access Token을 통해 사용자 정보 얻기
        KakaoUserInfo kakaoUserInfo = getKakaoUserInfo(accessToken);

        // 3. 우리 서비스에 가입된 유저가 아니면 회원 가입
        User user = userRepository.findByEmail(kakaoUserInfo.getKakaoAccount().getEmail())
                .orElseGet(() -> signup(kakaoUserInfo));

        if (user.getPhone() == null) { // phone이 null이면 카카오 로그인으로 새로 가입된 유저
            isNewUser = true;
        }

        String rawToken = jwtUtil.createToken(user.getId(), user.getEmail(), user.getName(), user.getRole());
        String token = jwtUtil.substringToken(rawToken);

        Cookie cookie = new Cookie("accessToken", token);
        
        cookie.setPath("/");
        cookie.setHttpOnly(true); // XSS 방어 코드
        cookie.setMaxAge(60 * 60);

        httpServletResponse.addCookie(cookie); // 쿠키에 실어 보냄

        return isNewUser;
    }

    /**
     * 카카오 로그인 후 토큰 정보 얻기
     */
    public KakaoLoginResponse getKakaoToken(String code) {

        // HTTP 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 바디 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("client_secret", clientSecret);
        body.add("code", code);

        // HTTP 요청에 필요한 헤더 + 바디 조립
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);

        // HTTP 요청을 보내는 메소드
        ResponseEntity<String> responseBody = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        KakaoLoginResponse response = null;

        try {
            response = objectMapper.readValue(responseBody.getBody(), KakaoLoginResponse.class);
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

    /**
     * 카카오 사용자 정보 얻기
     */
    public KakaoUserInfo getKakaoUserInfo(String accessToken) {

        // HTTP 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.add("Authorization", "Bearer " + accessToken);

        // HTTP 요청 조립하기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        
        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                kakaoUserInfoRequest,
                String.class
        );

        KakaoUserInfo kakaoUserInfo = null;

        try {
            kakaoUserInfo = objectMapper.readValue(response.getBody(), KakaoUserInfo.class);
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return kakaoUserInfo;
    }

    private User signup(KakaoUserInfo kakaoUserInfo) {
        User user = new User(
                null,
                kakaoUserInfo.getKakaoAccount().getProfile().getNickname(),
                kakaoUserInfo.getKakaoAccount().getEmail(),
                null,
                null,
                null,
                null
        );

        return userRepository.save(user);
    }
}
