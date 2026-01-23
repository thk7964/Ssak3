package com.example.ssak3.domain.inquirychat.entity;

import com.example.ssak3.common.entity.BaseEntity;
import com.example.ssak3.common.enums.ChatRoomStatus;
import com.example.ssak3.common.enums.InquiryStatus;
import com.example.ssak3.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "inquiry_chat_rooms")
@NoArgsConstructor
public class InquiryChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = true)
    private User admin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatRoomStatus status;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted = false;

    public InquiryChatRoom(User user, User admin, ChatRoomStatus status) {
        this.user = user;
        this.admin = admin;
        this.status = status;
    }

}
