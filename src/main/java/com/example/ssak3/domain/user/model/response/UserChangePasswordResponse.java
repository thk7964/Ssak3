package com.example.ssak3.domain.user.model.response;

import com.example.ssak3.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserChangePasswordResponse {

    private final Long userId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static UserChangePasswordResponse from(User user) {

        return new UserChangePasswordResponse(
                user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
