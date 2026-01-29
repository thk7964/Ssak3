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

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {  // 메시지가 전송되기 전에 실행

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);  // 헤더 정보 읽을 수 있도록 처리
        StompCommand command = accessor.getCommand();

        // 인증 (첫 연결(CONNECT)할 때 토큰 검증하기)
        if(StompCommand.CONNECT.equals(command)) {

            String authHeader = accessor.getFirstNativeHeader(JwtUtil.HEADER);  // Authorization

            // 토큰 존재 여부 확인
            if (authHeader == null || !authHeader.startsWith(JwtUtil.BEARER_PREFIX)) {
                throw new CustomException(ErrorCode.INVALID_TOKEN);
            }

            try {
                String token = jwtUtil.substringToken(authHeader);  // 순수 토큰만 추출

                Claims claims = jwtUtil.extractClaims(token);  // 토큰 유효성 검증 및 유저 정보 추출

                // 추출한 유저 정보를 STOMP 세션에 저장
                Map<String, Object> sessionAttributes = accessor.getSessionAttributes();

                if (sessionAttributes != null) {
                    sessionAttributes.put("userId", Long.parseLong(claims.getSubject()));
                    sessionAttributes.put("role", claims.get("role", String.class));
                }

            } catch (Exception e) {
                throw new CustomException(ErrorCode.STOMP_MESSAGE_ACCESS_FAILED);
            }
        }
        // 인가 (구독중인 방이 있는 경우 권한 체크)
        else if (StompCommand.SUBSCRIBE.equals(command)) {

            String destination = accessor.getDestination();

            if(destination != null && destination.startsWith("/sub/chat/room/")) {

                // 세션 정보 추출
                Map<String, Object> sessionAttributes = accessor.getSessionAttributes();

                if (sessionAttributes == null || sessionAttributes.get("userId") == null) {
                    throw new CustomException(ErrorCode.USER_NOT_FOUND);
                }

                Long userId = (Long) sessionAttributes.get("userId");
                String role = (String) sessionAttributes.get("role");

                // URL에서 roomId 가져오기
                Long roomId = Long.parseLong(destination.substring(destination.lastIndexOf("/") + 1));

                // DB 조회하여 권한 확인
                InquiryChatRoom foundRoom = roomRepository.findById(roomId)
                        .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

                boolean isAuthorized = false;

                // ROLE(USER / ADMIN)
                if ("USER".equals(role)) {
                    isAuthorized = foundRoom.getUser().getId().equals(userId);
                }
                else if("ADMIN".equals(role)) {

                    // 관리자가 아직 배정되지 않은 경우 접근 불가
                    if(foundRoom.getAdmin() == null) {
                        log.warn("{}번 채팅방은 아직 관리자가 배정되지 않았습니다. (접근 유저: {})", roomId, userId);
                        throw new CustomException(ErrorCode.CHAT_ROOM_ACCESS_DENIED);
                    }

                    isAuthorized = (foundRoom.getAdmin().getId().equals(userId));  // 배정된 관리자가 있는 경우 일치하는지 확인
                }

                if(!isAuthorized) {
                    log.warn("{}(Role:{})가 {}번 채팅방에 접근 시도!!", userId, role, roomId);
                    throw new CustomException(ErrorCode.CHAT_ROOM_ACCESS_DENIED);
                }
                log.info("{}(Role:{})가 {}번 채팅방에 연결 성공!!", userId, role, roomId);
            }
        }
        return message;
    }
}
