package com.example.ssak3.domain.inquirychat.model.request;

import com.example.ssak3.common.enums.ChatMessageType;
import com.example.ssak3.common.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class ChatMessageRequest {
    private ChatMessageType type;
    private Long roomId;
    private Long senderId;
    private UserRole senderRole;
    private String content;
}
