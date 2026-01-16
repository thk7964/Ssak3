package com.example.ssak3.domain.admin.model.request;

import com.example.ssak3.common.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminRoleChangeRequest {

    private UserRole role;
}
