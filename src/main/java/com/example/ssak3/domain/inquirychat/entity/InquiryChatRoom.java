package com.example.ssak3.domain.inquirychat.entity;

import com.example.ssak3.common.entity.BaseEntity;
import com.example.ssak3.common.enums.ChatRoomStatus;
import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "inquiry_chat_rooms")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InquiryChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
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

    public void assignAdmin(User admin) {

        if (this.admin != null && this.status != ChatRoomStatus.WAITING) {
            throw new CustomException(ErrorCode.CHAT_ROOM_ALREADY_ASSIGNED);
        }

        this.admin = admin;
        this.status = ChatRoomStatus.ONGOING;
    }

    public void chatComplete() {
        this.status = ChatRoomStatus.COMPLETED;
    }

}
