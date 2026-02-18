package com.example.ssak3.domain.inquiryreply.model.response;

import com.example.ssak3.common.enums.InquiryStatus;
import com.example.ssak3.domain.inquiryreply.entity.InquiryReply;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class InquiryReplyGetResponse {

    private final Long id;
    private final Long inquiryId;
    private final Long adminId;
    private final String content;
    private final InquiryStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static InquiryReplyGetResponse from(InquiryReply inquiryReply) {

        return new InquiryReplyGetResponse(
                inquiryReply.getId(),
                inquiryReply.getInquiry().getId(),
                inquiryReply.getAdmin().getId(),
                inquiryReply.getContent(),
                inquiryReply.getInquiry().getStatus(),
                inquiryReply.getCreatedAt(),
                inquiryReply.getUpdatedAt()
        );
    }

}
