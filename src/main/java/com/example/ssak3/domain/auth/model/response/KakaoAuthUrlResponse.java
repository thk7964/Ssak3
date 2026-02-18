package com.example.ssak3.domain.auth.model.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KakaoAuthUrlResponse {

    private final String kakaoAuthUrl;

    public static KakaoAuthUrlResponse from(String kakaoAuthUrl) {

        return new KakaoAuthUrlResponse(kakaoAuthUrl);
    }

}
