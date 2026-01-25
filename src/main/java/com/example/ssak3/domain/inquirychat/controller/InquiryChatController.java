package com.example.ssak3.domain.inquirychat.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.common.model.AuthUser;
import com.example.ssak3.domain.inquirychat.service.InquiryChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ssak3/chat/rooms")
public class InquiryChatController {

    private final InquiryChatService inquiryChatService;

    /**
     * 문의 채팅방 생성 API
     */
    @PostMapping
    public ResponseEntity<ApiResponse> createChatRoomApi(@AuthenticationPrincipal AuthUser user) {

        ApiResponse response = ApiResponse.success("문의 채팅방 연결 완료", inquiryChatService.createChatRoom(user.getId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 문의 채팅 메시지 조회 API
     */
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<ApiResponse> getChatHistoryApi(@AuthenticationPrincipal AuthUser sender , @PathVariable Long roomId) {

        ApiResponse response = ApiResponse.success("문의 채팅 내역 조회 완료", inquiryChatService.getChatHistory(roomId, sender.getId(), sender.getRole()));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



}
