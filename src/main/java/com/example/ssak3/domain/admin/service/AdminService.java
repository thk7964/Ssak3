package com.example.ssak3.domain.admin.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.UserRole;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.admin.model.request.AdminRegisterRequest;
import com.example.ssak3.domain.admin.model.response.AdminRegisterResponse;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    @Transactional
    public AdminRegisterResponse registerAdmin(AdminRegisterRequest request) {

        User user = userRepository.findById(request.getManagerId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.updateRole(UserRole.MANAGER);

        return AdminRegisterResponse.from(user);
    }
}
