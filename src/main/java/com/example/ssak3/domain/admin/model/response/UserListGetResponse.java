package com.example.ssak3.domain.admin.model.response;

import com.example.ssak3.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserListGetResponse {

    private final Long id;
    private final String name;
    private final String nickname;
    private final String email;

    public static UserListGetResponse from(User user) {
        return new UserListGetResponse(
                user.getId(),
                user.getName(),
                user.getNickname(),
                user.getEmail()
        );
    }
}
