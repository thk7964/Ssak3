package com.example.ssak3.domain.auth.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.common.utils.JwtUtil;
import com.example.ssak3.domain.auth.model.request.LoginRequest;
import com.example.ssak3.domain.auth.model.request.SignupRequest;
import com.example.ssak3.domain.auth.model.response.LoginResponse;
import com.example.ssak3.domain.auth.model.response.SignupResponse;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

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
                passwordEncoder.encode(request.getPassword()),
                request.getBirth(),
                request.getPhone(),
                request.getAddress());

        User savedUser = userRepository.save(user);

        return SignupResponse.from(savedUser);
    }

    /**
     * 로그인 비즈니스 로직
     */
    @Transactional
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.UNREGISTERED_USER));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        String accessToken = jwtUtil.createToken(user.getId(), user.getEmail(), user.getNickname(), user.getRole());

        return new LoginResponse(accessToken);
    }
}
