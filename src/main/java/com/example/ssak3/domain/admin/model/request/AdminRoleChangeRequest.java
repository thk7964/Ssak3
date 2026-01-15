package com.example.ssak3.domain.admin.model.request;

import com.example.ssak3.common.enums.UserRole;
import lombok.Getter;

@Getter
public class AdminRoleChangeRequest {

    private Long managerId;
    private UserRole role;
}
