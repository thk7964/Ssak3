package com.example.ssak3.domain.inquiryreply.model.response;

import com.example.ssak3.domain.inquiryreply.entity.InquiryReply;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class InquiryReplyCreateResponse {
    private final Long id;
    private final Long inquiryId;
    private final Long adminId;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static InquiryReplyCreateResponse from(InquiryReply inquiryReply) {
        return new InquiryReplyCreateResponse(
                inquiryReply.getId(),
                inquiryReply.getInquiry().getId(),
                inquiryReply.getAdmin().getId(),
                inquiryReply.getContent(),
                inquiryReply.getCreatedAt(),
                inquiryReply.getUpdatedAt()
        );
    }
}
