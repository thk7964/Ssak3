package com.example.ssak3.domain.admin.model.response;

import com.example.ssak3.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AdminRegisterResponse {

    private final Long managerId;
    private final String name;
    private final String phone;

    public static AdminRegisterResponse from(User user) {
        return new AdminRegisterResponse(
                user.getId(),
                user.getName(),
                user.getPhone()
        );
    }
}
