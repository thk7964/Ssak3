package com.example.ssak3.domain.inquirychat.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.common.model.AuthUser;
import com.example.ssak3.domain.inquirychat.service.InquiryChatAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ssak3/admin/chat")
public class InquiryChatAdminController {

    private final InquiryChatAdminService inquiryChatAdminService;

    /**
     * 관리자 문의 채팅방 참여 API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/rooms/{roomId}/accept")
    public ResponseEntity<ApiResponse> acceptChatApi(@AuthenticationPrincipal AuthUser admin, @PathVariable Long roomId) {

        ApiResponse response = ApiResponse.success("문의 배정 성공", inquiryChatAdminService.acceptChat(admin.getId(), roomId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 관리자 문의 채팅방 목록 조회 API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/rooms")
    public ResponseEntity<ApiResponse> getChatRoomsApi(@AuthenticationPrincipal AuthUser admin, @PageableDefault(size = 10) Pageable pageable){

       ApiResponse response = ApiResponse.success("문의채팅 목록 조회 성공", inquiryChatAdminService.getChatRoom(admin.getId(), pageable));

       return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
