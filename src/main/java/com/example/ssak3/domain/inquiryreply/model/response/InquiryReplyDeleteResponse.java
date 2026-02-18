package com.example.ssak3.domain.inquiryreply.model.response;

import com.example.ssak3.domain.inquiryreply.entity.InquiryReply;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class InquiryReplyDeleteResponse {

    private final Long id;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static InquiryReplyDeleteResponse from(InquiryReply inquiryReply) {

        return new InquiryReplyDeleteResponse(
                inquiryReply.getId(),
                inquiryReply.getCreatedAt(),
                inquiryReply.getUpdatedAt()
        );
    }
}
