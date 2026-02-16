package com.example.ssak3.domain.inquirychat.entity;

import com.example.ssak3.common.entity.BaseEntity;
import com.example.ssak3.common.enums.ChatMessageType;
import com.example.ssak3.common.enums.UserRole;
import com.example.ssak3.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "inquiry_chat_messages")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InquiryChatMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private InquiryChatRoom room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "sender_role")
    private UserRole senderRole;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatMessageType type;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted = false;

    public InquiryChatMessage(InquiryChatRoom room, User sender, UserRole senderRole, ChatMessageType type, String content) {
        this.room = room;
        this.sender = sender;
        this.senderRole = senderRole;
        this.type = type;
        this.content = content;
    }
}
