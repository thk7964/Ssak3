package com.example.ssak3.domain.user.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.common.model.AuthUser;
import com.example.ssak3.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
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
}