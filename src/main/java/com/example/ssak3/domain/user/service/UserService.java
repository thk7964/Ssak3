package com.example.ssak3.domain.user.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.common.model.AuthUser;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.user.model.response.MyProfileGetResponse;
import com.example.ssak3.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 마이 페이지 조회 비즈니스 로직
     */
    @Transactional(readOnly = true)
    public MyProfileGetResponse getMyProfile(AuthUser authUser) {

        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return MyProfileGetResponse.from(user);
    }
}