package com.example.ssak3.domain.auth.client;

import com.example.ssak3.domain.auth.model.response.KakaoAuthUrlResponse;
import com.example.ssak3.domain.auth.model.response.KakaoLoginResponse;
import com.example.ssak3.domain.auth.model.response.KakaoUserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class KakaoClient {

    @Value("${kakao.client_id}")
    private String clientId;

    @Value("${kakao.redirect_uri}")
    private String redirectUri;

    @Value("${kakao.client_secret}")
    private String clientSecret;

    private final WebClient webClient;

    /**
     * 카카오 로그인 URL 얻기
     */
    public KakaoAuthUrlResponse getAuthUrl() {

        String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize"
                + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code";

        return KakaoAuthUrlResponse.from(kakaoAuthUrl);
    }

    /**
     * 카카오 로그인 후 토큰 정보 얻기
     */
    public KakaoLoginResponse getKakaoToken(String code) {

        return webClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("client_id", clientId)
                        .with("redirect_uri", redirectUri)
                        .with("client_secret", clientSecret)
                        .with("code", code))
                .retrieve()
                .bodyToMono(KakaoLoginResponse.class)
                .block();
    }

    /**
     * 카카오 사용자 정보 얻기
     */
    public KakaoUserInfoResponse getKakaoUserInfo(String accessToken) {

        return webClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoUserInfoResponse.class)
                .block();
    }
}
