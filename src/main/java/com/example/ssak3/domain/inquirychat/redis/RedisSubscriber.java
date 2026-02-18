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

    public void onMessage(String savedMessage) {

        try {
            ChatMessageResponse response = objectMapper.readValue(savedMessage, ChatMessageResponse.class);

            messagingTemplate.convertAndSend("/sub/chat/room/" + response.getRoomId(), response);

        } catch (Exception e) {
            log.error("Redis 메시지 파싱 에러: {}", e.getMessage());
        }
    }
}
