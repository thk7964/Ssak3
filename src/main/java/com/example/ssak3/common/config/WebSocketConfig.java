package com.example.ssak3.common.config;

import com.example.ssak3.domain.inquirychat.handler.InquiryChatHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final InquiryChatHandler chatHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // ws://localhost:8080/ws/chat으로 접속 + 보안 적용시 wss://
        registry.addHandler(chatHandler, "/ssak3/chat")  // 아까 만들어 둔 핸들러 등록 + /ws/chat 주소로 접속
                .setAllowedOrigins("*");  // 모든 도메인 허용(테스트용, 추후 수정 예정)
    }
}

