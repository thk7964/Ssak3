package com.example.ssak3.domain.user.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.common.model.AuthUser;
import com.example.ssak3.domain.user.model.request.UserChangePasswordRequest;
import com.example.ssak3.domain.user.model.request.UserUpdateRequest;
import com.example.ssak3.domain.user.model.request.UserVerifyPasswordRequest;
import com.example.ssak3.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ssak3/users")
public class UserController {

    private final UserService userService;

    /**
     * 마이 페이지 조회 API
     */
    @GetMapping("/my-profile")
    public ResponseEntity<ApiResponse> getMyProfileApi(@AuthenticationPrincipal AuthUser authUser) {

        ApiResponse response = ApiResponse.success("마이 페이지 조회에 성공했습니다.", userService.getMyProfile(authUser));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 유저 정보 수정 API
     */
    @PatchMapping
    public ResponseEntity<ApiResponse> updateUserApi(@AuthenticationPrincipal AuthUser authUser, @RequestBody UserUpdateRequest request) {

        ApiResponse response = ApiResponse.success("유저 정보 수정에 성공했습니다.", userService.updateUser(authUser, request));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 비밀번호 검증 API
     */
    @PostMapping("/verify-password")
    public ResponseEntity<ApiResponse> verifyPasswordApi(@AuthenticationPrincipal AuthUser authUser, @RequestBody UserVerifyPasswordRequest request) {

        ApiResponse response = ApiResponse.success("비밀번호 검증 API가 성공적으로 호출되었습니다.", userService.verifyPassword(authUser, request));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 비밀번호 변경 API
     */
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse> changePasswordApi(@AuthenticationPrincipal AuthUser authUser, @RequestBody UserChangePasswordRequest request) {

        ApiResponse response = ApiResponse.success("비밀번호가 변경되었습니다.", userService.changePassword(authUser, request));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}