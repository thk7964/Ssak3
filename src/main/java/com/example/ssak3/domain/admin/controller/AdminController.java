package com.example.ssak3.domain.admin.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.domain.admin.model.request.AdminRoleChangeRequest;
import com.example.ssak3.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ssak3/admin")
public class AdminController {

    private final AdminService adminService;

    /**
     * 권한 변경 API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping
    public ResponseEntity<ApiResponse> changeUserRoleApi(@RequestBody AdminRoleChangeRequest request) {

        ApiResponse response = ApiResponse.success("권한을 변경했습니다.", adminService.changeUserRole(request));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 유저 전체 조회 API
     */
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/users")
    public ResponseEntity<ApiResponse> getUserListApi(@PageableDefault(sort = "nickname", direction = Sort.Direction.ASC) Pageable pageable) {

        ApiResponse response = ApiResponse.success("유저 전체 조회에 성공했습니다.", adminService.getUserList(pageable));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 유저 단 건 조회 API
     */
    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse> getUserApi(@PathVariable Long userId) {

        ApiResponse response = ApiResponse.success("유저 전체 조회에 성공했습니다.", adminService.getUser(userId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
