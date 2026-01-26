package com.example.ssak3.domain.inquirychat.handler;

import com.example.ssak3.domain.inquirychat.model.request.ChatMessageRequest;
import com.example.ssak3.domain.inquirychat.service.InquiryChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class InquiryChatHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final InquiryChatService inquiryChatService;
    private static Map<Long, Set<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();  // 방 번호, 세선 정보


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String payload = message.getPayload(); // 전송된 메시지 문자열로 가져옴

        ChatMessageRequest request = objectMapper.readValue(payload, ChatMessageRequest.class);  // JSON 변환

        Long roomId = request.getRoomId();  // 채팅방 입장

        // 타입별 분리
        switch (request.getType()) {
            case ENTER:
                // 방 세션 관리 및 입장
                handleEnterRoom(roomId, session);
                break;

            case SEND:
                // DB 저장 및 브로드캐스팅
                handleSendMessage(roomId, request);
                break;

            case QUIT:
                // 퇴장 알림 및 세션 정리
                handleQuitRoom(roomId, request, session);
                break;
        }
    }


    // ENTER: 세션 리스트에 추가
    private void handleEnterRoom(Long roomId, WebSocketSession session) {

        Set<WebSocketSession> sessions = roomSessions.get(roomId);

        // 방이 없는 경우 생성
        if (sessions == null) {
            sessions = Collections.newSetFromMap(new ConcurrentHashMap<>());

            roomSessions.put(roomId, sessions);
        }

        sessions.add(session);

         System.out.println("1:1 상담이 시작되었습니다.");
    }


    // SEND: DB 저장 후 방에 있는 모든 세션에게 메시지 전송
    private void handleSendMessage(Long roomId, ChatMessageRequest request) throws Exception {

        inquiryChatService.saveMessage(request); // DB 저장

        // 메시지 전달
        String output = request.getSenderRole() + ": " + request.getContent();

        broadcastToRoom(roomId, output);
    }


    // QUIT: 나간 세션 제거 및 채팅방 삭제
    private void handleQuitRoom(Long roomId, ChatMessageRequest request, WebSocketSession session) throws Exception {
        inquiryChatService.saveMessage(request);

        // 해당 세션 제거
        Set<WebSocketSession> sessions = roomSessions.get(roomId);
        if (sessions != null) {
            sessions.remove(session);

            // 관리자 / 회원의 상담 종료 알림
            broadcastToRoom(roomId, request.getSenderRole() + "님이 상담을 종료하셨습니다.");

            // 방 제거
            if (sessions.isEmpty()) {
                roomSessions.remove(roomId);
            }
        }

        // 웹소켓 연결 끊기
        if (session.isOpen()) {
            session.close();
        }

        System.out.println(roomId + "번 방 상담 종료 및 세션 정리 완료");
    }


    // 방에 있는 모든 세션에게 메시지 발송하는 메서드
    private void broadcastToRoom(Long roomId, String message) throws Exception {

        Set<WebSocketSession> sessions = roomSessions.get(roomId);

        if (sessions != null) {
            TextMessage textMessage = new TextMessage(message);
            for (WebSocketSession s : sessions) {
                if (s.isOpen()) {
                    s.sendMessage(textMessage);
                }
            }
        }
    }

    // 세션의 연결이 끊겼을 때 리스트에서 삭제 + 방 없애기
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        // 나간 세션들 지워주기
        for (Set<WebSocketSession> sessions : roomSessions.values()) {
            sessions.remove(session);
        }

        // 방이 빌 경우(모든 세션이 나간 경우) 방 자체를 삭제하기
        roomSessions.entrySet().removeIf(entry -> entry.getValue().isEmpty());

        System.out.println("연결 종료: " + session.getId());
    }
}
