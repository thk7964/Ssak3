package com.example.ssak3.domain.inquiry.entity;

import com.example.ssak3.common.entity.BaseEntity;
import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.InquiryStatus;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.inquiry.model.request.InquiryUpdateRequest;
import com.example.ssak3.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "inquiries")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inquiry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InquiryStatus status;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted = false;

    public Inquiry(User user, String title, String content, InquiryStatus status) {

        this.user = user;
        this.title = title;
        this.content = content;
        this.status = status;
    }

    public void update(InquiryUpdateRequest request) {

        if (request.getTitle() != null) {
            this.title = request.getTitle();
        }
        if (request.getContent() != null) {
            this.content = request.getContent();
        }
    }

    public void updateStatus(InquiryStatus status) {

        this.status = status;
    }

    public void softDelete() {

        this.isDeleted = true;
    }

    public void validateUser(Long userId) {

        if (!this.user.getId().equals(userId)) {
            throw new CustomException(ErrorCode.NOT_INQUIRY_WRITER);
        }
    }

    public void validateAnswered() {

        if (this.status == InquiryStatus.ANSWERED) {
            throw new CustomException(ErrorCode.INQUIRY_ALREADY_ANSWERED);
        }
    }
}
