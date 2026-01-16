package com.example.ssak3.domain.admin.model.response;

import com.example.ssak3.common.enums.UserRole;
import com.example.ssak3.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AdminRoleChangeResponse {

    private final Long userId;
    private final String name;
    private final String phone;
    private final UserRole role;

    public static AdminRoleChangeResponse from(User user) {
        return new AdminRoleChangeResponse(
                user.getId(),
                user.getName(),
                user.getPhone(),
                user.getRole()
        );
    }
}
