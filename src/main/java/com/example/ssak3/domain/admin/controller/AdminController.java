package com.example.ssak3.domain.admin.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.domain.admin.model.request.AdminRoleChangeRequest;
import com.example.ssak3.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ssak3/admin")
public class AdminController {

    private final AdminService adminService;

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping
    public ResponseEntity<ApiResponse> changeUserRoleApi(@RequestBody AdminRoleChangeRequest request) {

        ApiResponse response = ApiResponse.success("관리자 권한으로 변경했습니다.", adminService.changeUserRole(request));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
