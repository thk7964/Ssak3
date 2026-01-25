package com.example.ssak3.domain.inquirychat.entity;

import com.example.ssak3.common.entity.BaseEntity;
import com.example.ssak3.common.enums.ChatRoomStatus;
import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.InquiryStatus;
import com.example.ssak3.common.exception.CustomException;
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

    // 관리자 배정
    public void assignAdmin(User admin) {
        // 관리자가 배정되지 않았거나 문의대기 상태인 것만 관리자 배정 가능
        if (this.admin != null || this.status != ChatRoomStatus.WAITING) {
            throw new CustomException(ErrorCode.CHAT_ROOM_ALREADY_ASSIGNED);
        }
        this.admin = admin;
        this.status = ChatRoomStatus.ONGOING;
    }

    // 문의 채팅 종료 시 COMPLETED로 상태 변경
    public void chatComplete() {
        this.status = ChatRoomStatus.COMPLETED;
    }

}
