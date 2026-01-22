package com.example.ssak3.domain.user.repository;

import com.example.ssak3.common.enums.UserRole;
import com.example.ssak3.domain.admin.model.response.UserListGetResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserCustomRepository {

    Page<UserListGetResponse> getUserList(UserRole role, String keyword, Pageable pageable);
}