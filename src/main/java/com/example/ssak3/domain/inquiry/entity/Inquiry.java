package com.example.ssak3.domain.inquiry.entity;

import com.example.ssak3.common.entity.BaseEntity;
import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
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

    @Column(nullable = false)
    private String status;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted = false;

    public Inquiry(User user, String title, String content, String status) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.status = status;
    }

    public void validateUser(Long userId) {
        if (!this.user.getId().equals(userId)) {
            throw new CustomException(ErrorCode.NOT_INQUIRY_WRITER);
        }
    }

}
