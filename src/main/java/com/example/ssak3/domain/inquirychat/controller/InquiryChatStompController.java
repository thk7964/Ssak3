package com.example.ssak3.domain.inquirychat.controller;

import com.example.ssak3.common.enums.ChatMessageType;
import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.common.model.AuthUser;
import com.example.ssak3.domain.inquirychat.model.request.ChatMessageRequest;
import com.example.ssak3.domain.inquirychat.model.response.ChatMessageResponse;
import com.example.ssak3.domain.inquirychat.service.InquiryChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;

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
    public void sendMessage(@Payload ChatMessageRequest request, Principal principal) {

        if (!(principal instanceof Authentication authentication)) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        Object principalObj = authentication.getPrincipal();

        if (!(principalObj instanceof AuthUser authUser)) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        Long userId = authUser.getId();
        String role = authUser.getRole().name();

        ChatMessageResponse tempResponse = ChatMessageResponse.fromRequest(request, userId, role);

        chatRedisTemplate.convertAndSend(chatTopic.getTopic(), tempResponse);

        inquiryChatService.saveMessageAsync(request, userId, role);
    }

    /**
     * 문의 채팅방 입장, 퇴장 알림 API
     */
    @MessageMapping("/chat/notice")
    public void sendNotice(@Payload ChatMessageRequest request) {

        String message = (request.getType() == ChatMessageType.ENTER)
                ? request.getSenderRole() + "님이 입장했습니다."
                : request.getSenderRole() + "님이 문의를 종료했습니다.";

        ChatMessageResponse response = ChatMessageResponse.from(request, message);

        chatRedisTemplate.convertAndSend(chatTopic.getTopic(), response);
    }
}
