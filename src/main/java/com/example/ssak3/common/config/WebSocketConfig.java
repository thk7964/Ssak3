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
        // 메시지를 보낼 때 Publish 설정
        // 클라이언트가 메시지를 보낼 때 /pub으로 시작하면 @MessageMapping이 가로챔
        registry.setApplicationDestinationPrefixes("/pub");

        // 메시지를 받을 때 Subscribe 설정
        // 클라이언트가 /sub 주소를 구독하고 있으면 서버가 메시지를 해당 주소로 보내줌
        registry.enableSimpleBroker("/sub");  // 추후에 Redis로 확장
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }
}

