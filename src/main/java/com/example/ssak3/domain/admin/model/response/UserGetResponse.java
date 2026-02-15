package com.example.ssak3.domain.admin.model.response;

import com.example.ssak3.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserGetResponse {

    private final Long id;
    private final String name;
    private final String nickname;
    private final String email;
    private final String phone;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static UserGetResponse from(User user) {

        return new UserGetResponse(
                user.getId(),
                user.getName(),
                user.getNickname(),
                user.getEmail(),
                user.getPhone(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

}
