package com.example.ssak3.domain.user.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
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

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 마이 페이지 조회
     */
    @Transactional(readOnly = true)
    public MyProfileGetResponse getMyProfile(Long userId) {

        User user = getUser(userId);

        return MyProfileGetResponse.from(user);
    }



    /**
     * 유저 정보 수정
     */
    @Transactional
    public UserUpdateResponse updateUser(Long userId, UserUpdateRequest request) {

        User user = getUser(userId);

        String nickname = request.getNickname().replaceAll("\\s", "");
        String phone = request.getPhone().replaceAll("\\s", "");

        if (userRepository.existsByNickname(nickname) && !user.getNickname().equals(nickname)) {
            throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }

        if (userRepository.existsByPhone(phone) && !user.getPhone().equals(phone)) {
            throw new CustomException(ErrorCode.PHONE_ALREADY_EXISTS);
        }

        if (request.getBirth().isAfter(LocalDate.now())) {
            throw new CustomException(ErrorCode.INVALID_BIRTH);
        }

        user.update(request);

        return UserUpdateResponse.from(user);
    }

    /**
     * 비밀번호 검증
     */
    public UserVerifyPasswordResponse verifyPassword(Long userId, UserVerifyPasswordRequest request) {

        User user = getUser(userId);

        return new UserVerifyPasswordResponse(passwordEncoder.matches(request.getPassword(), user.getPassword()));
    }

    /**
     * 비밀번호 변경
     */
    @Transactional
    public UserChangePasswordResponse changePassword(Long userId, UserChangePasswordRequest request) {

        User user = getUser(userId);

        user.updatePassword(passwordEncoder.encode(request.getNewPassword()));

        return UserChangePasswordResponse.from(user);
    }

    /**
     * 유저 탈퇴
     */
    @Transactional
    public UserDeleteResponse deleteUser(Long userId) {

        User user = getUser(userId);

        user.softDelete();

        user.updatePhone();

        return UserDeleteResponse.from(user);
    }

    /**
     * 유저를 얻어오는 내부 전용 메소드
     */
    private User getUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (user.isDeleted()) {
            throw new CustomException(ErrorCode.WITHDRAWN_USER);
        }

        return user;
    }
}