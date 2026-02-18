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

    /**
     * 회원 권한 변경
     */
    @Transactional
    public AdminRoleChangeResponse changeUserRole(Long userId, AdminRoleChangeRequest request) {

        if (request.getRole().equals(UserRole.SUPER_ADMIN)) {
            throw new CustomException(ErrorCode.NOT_ALLOWED_CHANGE_SUPER_ADMIN);
        }

        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.updateRole(request.getRole());

        return AdminRoleChangeResponse.from(user);
    }

    /**
     * 회원 목록 조회
     */
    @Transactional(readOnly = true)
    public PageResponse<UserListGetResponse> getUserList(UserRole role, String nickname, Pageable pageable) {

        Page<UserListGetResponse> userList = userRepository.getUserList(role, nickname, pageable);

        return PageResponse.from(userList);
    }

    /**
     * 회원 단 건 조회
     */
    @Transactional(readOnly = true)
    public UserGetResponse getUser(Long userId) {

        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return UserGetResponse.from(user);
    }
}
