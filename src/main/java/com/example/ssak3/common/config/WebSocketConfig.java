package com.example.ssak3.common.config;

import com.example.ssak3.domain.inquirychat.handler.InquiryChatStompHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final InquiryChatStompHandler stompHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 처음 소켓 연결할 엔드포인트 설정
        registry.addEndpoint("/ssak3/stomp/chat")  // STOMP 전용 주소 사용
                .setAllowedOrigins(
                        "http://localhost:8080",
                        "http://localhost:63342",
                        "https://ssak3.store",
                        "https://www.ssak3.store",
                        "https://admin.ssak3.store"
                );
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        registry.setApplicationDestinationPrefixes("/pub");
        registry.enableSimpleBroker("/sub");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {

        registration.interceptors(stompHandler);
    }
}

