package com.example.ssak3.domain.admin.model.response;

import com.example.ssak3.common.enums.UserRole;
import com.example.ssak3.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserListGetResponse {

    private final Long id;
    private final String name;
    private final String nickname;
    private final UserRole role;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static UserListGetResponse from(User user) {

        return new UserListGetResponse(
                user.getId(),
                user.getName(),
                user.getNickname(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
