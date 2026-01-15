package com.example.ssak3.domain.user.model.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserChangePasswordResponse {

    private final Long userId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
