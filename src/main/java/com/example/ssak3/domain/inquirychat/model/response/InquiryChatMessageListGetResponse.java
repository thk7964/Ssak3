package com.example.ssak3.domain.inquirychat.model.response;

import com.example.ssak3.common.enums.UserRole;
import com.example.ssak3.domain.inquirychat.entity.InquiryChatMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class InquiryChatMessageListGetResponse {

    private final String content;
    private final Long senderId;
    private final UserRole senderRole;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static InquiryChatMessageListGetResponse from(InquiryChatMessage message) {

        return new InquiryChatMessageListGetResponse(
                message.getContent(),
                message.getSender().getId(),
                message.getSenderRole(),
                message.getCreatedAt(),
                message.getUpdatedAt()
        );
    }
}
