package com.example.ssak3.domain.inquiryreply.entity;

import com.example.ssak3.common.entity.BaseEntity;
import com.example.ssak3.domain.inquiry.entity.Inquiry;
import com.example.ssak3.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "inquiry_replies")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InquiryReply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private User admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquiry_id")
    private Inquiry inquiry;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted = false;

    public InquiryReply(User admin, Inquiry inquiry, String content) {
        this.admin = admin;
        this.inquiry = inquiry;
        this.content = content;
    }

    public void update(User newAdmin, String newContent) {

        this.admin = newAdmin;
        this.content = newContent;
    }

    public void softDelete() {

        this.isDeleted = true;
    }
}
