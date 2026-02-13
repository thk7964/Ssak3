package com.example.ssak3.domain.inquirychat.controller;

import com.example.ssak3.common.enums.ChatMessageType;
import com.example.ssak3.domain.inquirychat.model.request.ChatMessageRequest;
import com.example.ssak3.domain.inquirychat.model.response.ChatMessageResponse;
import com.example.ssak3.domain.inquirychat.service.InquiryChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class InquiryChatStompController {

    private final InquiryChatService inquiryChatService;
    private final RedisTemplate<String, Object> chatRedisTemplate;
    private final ChannelTopic chatTopic = new ChannelTopic("chat");

    /**
     * 채팅 메시지 전송 API
     */
    @MessageMapping("/chat/message")
    public void sendMessage(@Payload ChatMessageRequest request, StompHeaderAccessor accessor) {
        // StompHeaderAccessor: 웹소켓 메시지 안에 숨겨진 세션 정보, 인증 정보, 헤더 값들을 읽거나 수정

        // 핸들러의 세션에서 인증된 유저 정보 꺼내기
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
        Long userId = (Long) sessionAttributes.get("userId");
        String role = (String) sessionAttributes.get("role");

        // Redis 먼저 발행
        ChatMessageResponse tempResponse = ChatMessageResponse.fromRequest(request, userId, role);

        chatRedisTemplate.convertAndSend(chatTopic.getTopic(), tempResponse);

        // DB 저장은 비동기로 처리
        inquiryChatService.saveMessageAsync(request, userId, role);
    }


    /**
     * 문의 채팅방 입장, 퇴장 알림 API
     */
    @MessageMapping("/chat/notice")
    public void sendNotice(@Payload ChatMessageRequest request) {

        // 채팅방 입장, 퇴장 시 알림으로 보내 줄 메시지
        String message = (request.getType() == ChatMessageType.ENTER)
                ? request.getSenderRole() + "님이 입장했습니다."
                : request.getSenderRole() + "님이 문의를 종료했습니다.";

        ChatMessageResponse response = ChatMessageResponse.from(request, message);

        chatRedisTemplate.convertAndSend(chatTopic.getTopic(), response);  // "chat"으로 메시지 발행
    }
}
