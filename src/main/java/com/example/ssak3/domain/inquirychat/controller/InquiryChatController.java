package com.example.ssak3.domain.inquirychat.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.common.model.AuthUser;
import com.example.ssak3.domain.inquirychat.service.InquiryChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ssak3/chat")
public class InquiryChatController {

    private final InquiryChatService inquiryChatService;

    /**
     * 문의 채팅방 생성 API
     */
    @PostMapping("/rooms")
    public ResponseEntity<ApiResponse> createChatRoomApi(@AuthenticationPrincipal AuthUser user) {
        Long userId = user.getId();
        ApiResponse response = ApiResponse.success("문의 채팅방이 생성되었습니다.", inquiryChatService.createChatRoom(userId));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
