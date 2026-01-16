package com.example.ssak3.domain.admin.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.UserRole;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.common.model.PageResponse;
import com.example.ssak3.domain.admin.model.request.AdminRoleChangeRequest;
import com.example.ssak3.domain.admin.model.response.AdminRoleChangeResponse;
import com.example.ssak3.domain.admin.model.response.UserGetResponse;
import com.example.ssak3.domain.admin.model.response.UserListGetResponse;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    @Transactional
    public AdminRoleChangeResponse changeUserRole(Long userId, AdminRoleChangeRequest request) {

        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.updateRole(request.getRole());

        return AdminRoleChangeResponse.from(user);
    }

    @Transactional(readOnly = true)
    public PageResponse<UserListGetResponse> getUserList(UserRole role, Pageable pageable) {

        Page<UserListGetResponse> userList = userRepository.findUserByRole(role, false, pageable).map(UserListGetResponse::from);

        return PageResponse.from(userList);
    }

    @Transactional(readOnly = true)
    public UserGetResponse getUser(Long userId) {

        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return UserGetResponse.from(user);
    }
}
