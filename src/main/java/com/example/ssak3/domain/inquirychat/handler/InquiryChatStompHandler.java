package com.example.ssak3.domain.inquirychat.handler;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.UserRole;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.common.model.AuthUser;
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
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;
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
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                setAuthenticationToAccessor(accessor, token);
            } else {
                Map<String, Object> sessionAttributes = accessor.getSessionAttributes();

                if (sessionAttributes != null && sessionAttributes.containsKey("accessToken")) {
                    String token = (String) sessionAttributes.get("accessToken");
                    setAuthenticationToAccessor(accessor, token);
                }
            }
        }

        if (accessor.getUser() == null) {
            Map<String, Object> sessionAttributes = accessor.getSessionAttributes();

            if (sessionAttributes != null) {
                Authentication auth = (Authentication) sessionAttributes.get("userPrincipal");

                if (auth != null) {
                    accessor.setUser(auth);
                }
            }
        }

        if (StompCommand.SUBSCRIBE.equals(command)) {

            String destination = accessor.getDestination();

            if (destination != null && destination.startsWith("/sub/chat/room/")) {

                Principal principal = accessor.getUser();

                if (principal == null) {
                    Map<String, Object> sessionAttributes = accessor.getSessionAttributes();

                    if (sessionAttributes != null) {
                        principal = (Authentication) sessionAttributes.get("userPrincipal");

                        if (principal != null) {
                            accessor.setUser(principal);
                        }
                    }
                }

                if (principal == null) {
                    throw new CustomException(ErrorCode.USER_NOT_FOUND);
                }

                AuthUser authUser = (AuthUser) ((Authentication) principal).getPrincipal();

                Long userId = authUser.getId();
                Long roomId = Long.parseLong(destination.substring(destination.lastIndexOf("/") + 1));

                InquiryChatRoom foundRoom = roomRepository.findById(roomId)
                        .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

                boolean isAuthorized = foundRoom.getUser().getId().equals(userId) || (foundRoom.getAdmin() != null && foundRoom.getAdmin().getId().equals(userId));

                if (!isAuthorized) {
                    log.warn("{}가 {}번 채팅방 접근 시도", userId, roomId);
                    throw new CustomException(ErrorCode.CHAT_ROOM_ACCESS_DENIED);
                }

                log.info("{}가 {}번 채팅방 연결 성공", userId, roomId);
            }

        }

        return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());
    }

    private void setAuthenticationToAccessor(StompHeaderAccessor accessor, String token) {
        Claims claims = jwtUtil.extractClaims(token);
        Long userId = Long.parseLong(claims.getSubject());
        String role = claims.get("role", String.class);

        AuthUser authUser = new AuthUser(userId, null, UserRole.valueOf(role));
        Authentication auth = new UsernamePasswordAuthenticationToken(
                authUser, null, List.of(new SimpleGrantedAuthority("ROLE_" + role)));

        SecurityContextHolder.getContext().setAuthentication(auth);
        accessor.setUser(auth);

        if (accessor.getSessionAttributes() != null) {
            accessor.getSessionAttributes().put("userPrincipal", auth);
        }

        log.info("인증 설정 완료 : userId={} role={}", userId, role);
    }
}
