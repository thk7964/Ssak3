package com.example.ssak3.domain.inquirychat.handler;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.common.utils.JwtUtil;
import com.example.ssak3.domain.inquirychat.entity.InquiryChatRoom;
import com.example.ssak3.domain.inquirychat.repository.InquiryChatRoomRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class InquiryChatStompHandler implements ChannelInterceptor {

    private final JwtUtil jwtUtil;
    private final InquiryChatRoomRepository roomRepository;

    /**
     * STOMP WebSocket 메시지 전송 전 처리 인터셉터
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();

        if (StompCommand.CONNECT.equals(command)) {

            String authHeader = accessor.getFirstNativeHeader(JwtUtil.HEADER);

            if (authHeader == null || !authHeader.startsWith(JwtUtil.BEARER_PREFIX)) {
                throw new CustomException(ErrorCode.INVALID_TOKEN);
            }

            try {
                String token = jwtUtil.substringToken(authHeader);

                Claims claims = jwtUtil.extractClaims(token);

                Map<String, Object> sessionAttributes = accessor.getSessionAttributes();

                if (sessionAttributes != null) {
                    sessionAttributes.put("userId", Long.parseLong(claims.getSubject()));
                    sessionAttributes.put("role", claims.get("role", String.class));
                }

            } catch (Exception e) {
                throw new CustomException(ErrorCode.STOMP_MESSAGE_ACCESS_FAILED);
            }
        } else if (StompCommand.SUBSCRIBE.equals(command)) {

            String destination = accessor.getDestination();

            if (destination != null && destination.startsWith("/sub/chat/room/")) {

                Map<String, Object> sessionAttributes = accessor.getSessionAttributes();

                if (sessionAttributes == null || sessionAttributes.get("userId") == null) {
                    throw new CustomException(ErrorCode.USER_NOT_FOUND);
                }

                Long userId = (Long) sessionAttributes.get("userId");
                String role = (String) sessionAttributes.get("role");

                Long roomId = Long.parseLong(destination.substring(destination.lastIndexOf("/") + 1));

                InquiryChatRoom foundRoom = roomRepository.findById(roomId)
                        .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

                boolean isAuthorized = false;

                if ("USER".equals(role)) {
                    isAuthorized = foundRoom.getUser().getId().equals(userId);
                } else if ("ADMIN".equals(role)) {

                    if (foundRoom.getAdmin() == null) {
                        log.warn("{}번 채팅방은 아직 관리자가 배정되지 않았습니다. (접근 유저: {})", roomId, userId);
                        throw new CustomException(ErrorCode.CHAT_ROOM_ACCESS_DENIED);
                    }

                    isAuthorized = (foundRoom.getAdmin().getId().equals(userId));
                }

                if (!isAuthorized) {
                    log.warn("{} (Role: {})가 {}번 채팅방에 접근 시도", userId, role, roomId);
                    throw new CustomException(ErrorCode.CHAT_ROOM_ACCESS_DENIED);
                }
                log.info("{} (Role: {})가 {}번 채팅방에 연결 성공!!", userId, role, roomId);
            }
        }
        return message;
    }
}
