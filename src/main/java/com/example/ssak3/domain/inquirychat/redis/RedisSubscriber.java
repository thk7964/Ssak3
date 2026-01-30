package com.example.ssak3.domain.inquirychat.redis;

import com.example.ssak3.domain.inquirychat.model.response.ChatMessageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    // Redis에서 메시지가 발행되면 이 메서드가 자동으로 실행
    public void onMessage(String savedMessage) {
        try {
            // Redis에서 넘어온 JSON 문자열 DTO로 변환
            ChatMessageResponse response = objectMapper.readValue(savedMessage, ChatMessageResponse.class);

            // 해당 방을 구독 중인 유저들에게 최종적으로 메시지 전송
            messagingTemplate.convertAndSend("/sub/chat/room/" + response.getRoomId(), response);

        } catch (Exception e) {
            log.error("Redis 메시지 파싱 에러: {}", e.getMessage());
        }
    }
}
