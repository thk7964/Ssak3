package com.example.ssak3.domain.user.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.common.model.AuthUser;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.user.model.request.UserChangePasswordRequest;
import com.example.ssak3.domain.user.model.request.UserUpdateRequest;
import com.example.ssak3.domain.user.model.request.UserVerifyPasswordRequest;
import com.example.ssak3.domain.user.model.response.*;
import com.example.ssak3.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 마이 페이지 조회 비즈니스 로직
     */
    @Transactional(readOnly = true)
    public MyProfileGetResponse getMyProfile(AuthUser authUser) {

        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return MyProfileGetResponse.from(user);
    }

    /**
     * 유저 정보 수정 비즈니스 로직
     */
    @Transactional
    public UserUpdateResponse updateUser(AuthUser authUser, UserUpdateRequest request) {

        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!request.getNickname().equals(authUser.getNickname()) && userRepository.existsByNickname(request.getNickname())) {
            throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }

        user.update(request);

        return UserUpdateResponse.from(user);
    }

    /**
     * 비밀번호 검증 비즈니스 로직
     */
    public UserVerifyPasswordResponse verifyPassword(AuthUser authUser, UserVerifyPasswordRequest request) {

        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return new UserVerifyPasswordResponse(passwordEncoder.matches(request.getPassword(), user.getPassword()));
    }

    /**
     * 비밀번호 변경 비즈니스 로직
     */
    @Transactional
    public UserChangePasswordResponse changePassword(AuthUser authUser, UserChangePasswordRequest request) {

        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.updatePassword(passwordEncoder.encode(request.getNewPassword()));

        return UserChangePasswordResponse.from(user);
    }

    /**
     * 회원 탈퇴 비즈니스 로직
     */
    @Transactional
    public UserDeleteResponse deleteUser(AuthUser authUser) {

        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.softDelete();

        return UserDeleteResponse.from(user);
    }
}