package com.example.ssak3.domain.inquirychat.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.common.model.AuthUser;
import com.example.ssak3.domain.inquirychat.service.InquiryChatAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ssak3/admin/chat")
public class InquiryChatAdminController {

    private final InquiryChatAdminService inquiryChatAdminService;

    /**
     * 관리자 문의 채팅방 참여 API
     */
    @PatchMapping("/rooms/{roomId}/accept")
    public ResponseEntity<ApiResponse> acceptChatApi(@AuthenticationPrincipal AuthUser admin, @PathVariable Long roomId) {

        ApiResponse response = ApiResponse.success("문의가 배정되었습니다.", inquiryChatAdminService.acceptChat(admin.getId(), roomId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



}
