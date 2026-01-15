package com.example.ssak3.domain.auth.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.auth.model.request.SignupRequest;
import com.example.ssak3.domain.auth.model.response.SignupResponse;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    /**
     * 회원 가입 비즈니스 로직
     */
    @Transactional
    public SignupResponse signup(SignupRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        User user = new User(
                request.getName(),
                request.getNickname(),
                request.getEmail(),
                request.getPassword(),
                request.getBirth(),
                request.getPhone(),
                request.getAddress());

        User savedUser = userRepository.save(user);

        return SignupResponse.from(savedUser);
    }
}
