package com.example.ssak3.domain.inquirychat.controller;

import com.example.ssak3.common.enums.ChatMessageType;
import com.example.ssak3.domain.inquirychat.model.request.ChatMessageRequest;
import com.example.ssak3.domain.inquirychat.service.InquiryChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class InquiryChatStompController {

    private final InquiryChatService inquiryChatService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 채팅 메시지 전송 API
     */
    @MessageMapping("/chat/message")
    public void sendMessage(@Payload ChatMessageRequest request, StompHeaderAccessor accessor) {

        // 핸들러의 세션에서 인증된 유저 정보 꺼내기
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
        Long authenticatedUserId = (Long) sessionAttributes.get("userId");
        String role = (String) sessionAttributes.get("role");

        inquiryChatService.saveMessage(request, authenticatedUserId, role);

        // 브로드캐스팅
        messagingTemplate.convertAndSend("/sub/chat/room/" + request.getRoomId(), request);
    }


    /**
     * 문의 채팅방 입장, 퇴장 알림 API
     */
    @MessageMapping("/chat/notice")
    public void sendNotice(@Payload ChatMessageRequest request) {
        String message = "";  // 채팅방 입장, 퇴장 시 알림으로 보내 줄 메시지

        if (request.getType() == ChatMessageType.ENTER) {
            message = request.getSenderRole() + "님이 입장했습니다.";
        } else if (request.getType() == ChatMessageType.QUIT) {
            message = request.getSenderRole() + "님이 문의를 종료했습니다.";
        }
        messagingTemplate.convertAndSend("/sub/chat/room/" + request.getRoomId(), message);
    }
}
