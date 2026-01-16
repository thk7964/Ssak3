package com.example.ssak3.domain.user.model.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserVerifyPasswordResponse {

    private final Boolean isPasswordMatch;
}
