package com.example.ssak3.common.model;

import com.example.ssak3.common.enums.UserRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthUser {

    private final Long userId;
    private final String email;
    private final String nickname;
    private final UserRole role;

}
