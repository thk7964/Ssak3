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

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
    @Transactional
    public SignupResponse signup(SignupRequest request) {

        String name = request.getName().replaceAll("\\s", "");
        String nickname = request.getNickname().replaceAll("\\s", "");
        String email = request.getEmail().replaceAll("\\s", "");
        String password = request.getPassword().replaceAll("\\s", "");
        String phone = request.getPhone().replaceAll("\\s", "");

        if (userRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        if (userRepository.existsByNickname(nickname)) {
            throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }

        if (userRepository.existsByPhone(request.getPhone())) {
            throw new CustomException(ErrorCode.PHONE_ALREADY_EXISTS);
        }

        if (request.getBirth().isAfter(LocalDate.now())) {
            throw new CustomException(ErrorCode.INVALID_BIRTH);
        }

        User user = new User(
                name,
                nickname,
                email,
                passwordEncoder.encode(password),
                request.getBirth(),
                phone,
                request.getAddress());

        User savedUser = userRepository.save(user);

        return SignupResponse.from(savedUser);
    }

    /**
     * 로그인
     */
    @Transactional
    public LoginResponse login(LoginRequest request) {

        if (!userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.UNREGISTERED_USER);
        }

        User user = userRepository.findByEmailAndIsDeletedFalse(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.WITHDRAWN_USER));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        String accessToken = jwtUtil.createToken(user.getId(), user.getEmail(), user.getRole());

        return new LoginResponse(accessToken);
    }
}
