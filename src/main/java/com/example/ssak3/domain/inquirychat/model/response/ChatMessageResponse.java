package com.example.ssak3.domain.inquirychat.model.response;

import com.example.ssak3.common.enums.ChatMessageType;
import com.example.ssak3.common.enums.UserRole;
import com.example.ssak3.domain.inquirychat.entity.InquiryChatMessage;
import com.example.ssak3.domain.inquirychat.model.request.ChatMessageRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class ChatMessageResponse {

    private final Long roomId;
    private final Long senderId;
    private final UserRole senderRole;
    private final ChatMessageType type;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    // 채팅 메시지 Response(DB 조회 메시지)
    public static ChatMessageResponse from(InquiryChatMessage inquiryChatMessage) {

        return new ChatMessageResponse(
                inquiryChatMessage.getRoom().getId(),
                inquiryChatMessage.getSender().getId(),
                inquiryChatMessage.getSenderRole(),
                inquiryChatMessage.getType(),
                inquiryChatMessage.getContent(),
                inquiryChatMessage.getCreatedAt(),
                inquiryChatMessage.getUpdatedAt()
        );
    }

    // 공지 메시지 Response
    public static ChatMessageResponse from(ChatMessageRequest request, String noticeMessage) {

        return new ChatMessageResponse(
                request.getRoomId(),
                0L,
                request.getSenderRole(),
                request.getType(),
                noticeMessage,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    // 메시지 요청으로부터 임시 Response 생성(DB 저장 전 Redis로 먼저 보냄)
    public static ChatMessageResponse fromRequest(ChatMessageRequest request, Long userId, String role) {

        return new ChatMessageResponse(
                request.getRoomId(),
                userId,
                UserRole.valueOf(role),
                request.getType(),
                request.getContent(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}
