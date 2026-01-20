package com.example.ssak3.domain.admin.controller;

import com.example.ssak3.common.enums.UserRole;
import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.domain.admin.model.request.AdminRoleChangeRequest;
import com.example.ssak3.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ssak3/admin/users")
public class AdminController {

    private final AdminService adminService;

    /**
     * 권한 변경 API
     */
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PatchMapping("/{userId}")
    public ResponseEntity<ApiResponse> changeUserRoleApi(
            @PathVariable Long userId,
            @RequestBody AdminRoleChangeRequest request) {

        ApiResponse response = ApiResponse.success("권한을 변경했습니다.", adminService.changeUserRole(userId, request));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 유저 목록 조회 API
     * 쿼리 파라미터 role - SUPER_ADMIN, ADMIN, USER, 닉네임 검색
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse> getUserListApi(
            @RequestParam(required = false) UserRole role,
            @RequestParam(required = false) String nickname,
            @PageableDefault Pageable pageable) {

        ApiResponse response = ApiResponse.success("유저 목록 조회에 성공했습니다.", adminService.getUserList(role, nickname, pageable));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 유저 단 건 조회 API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getUserApi(@PathVariable Long userId) {

        ApiResponse response = ApiResponse.success("유저 단 건 조회에 성공했습니다.", adminService.getUser(userId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
