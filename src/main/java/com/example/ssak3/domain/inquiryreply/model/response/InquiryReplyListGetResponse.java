package com.example.ssak3.domain.inquiryreply.model.response;

import com.example.ssak3.common.enums.InquiryStatus;
import com.example.ssak3.domain.inquiryreply.entity.InquiryReply;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class InquiryReplyListGetResponse {
    private final Long id;
    private final Long inquiryId;
    private final Long adminId;
    private final InquiryStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static InquiryReplyListGetResponse from(InquiryReply inquiryReply) {
        return new InquiryReplyListGetResponse(
                inquiryReply.getId(),
                inquiryReply.getInquiry().getId(),
                inquiryReply.getAdmin().getId(),
                inquiryReply.getInquiry().getStatus(),
                inquiryReply.getCreatedAt(),
                inquiryReply.getUpdatedAt()
        );
    }
}
