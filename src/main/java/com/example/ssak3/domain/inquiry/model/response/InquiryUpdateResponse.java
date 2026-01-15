package com.example.ssak3.domain.inquiry.model.response;

import com.example.ssak3.common.enums.InquiryStatus;
import com.example.ssak3.domain.inquiry.entity.Inquiry;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class InquiryUpdateResponse {
    private final Long id;
    private final Long userId;
    private final String title;
    private final String content;
    private final InquiryStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static InquiryUpdateResponse from(Inquiry inquiry) {
        return new InquiryUpdateResponse(
                inquiry.getId(),
                inquiry.getUser().getId(),
                inquiry.getTitle(),
                inquiry.getContent(),
                inquiry.getStatus(),
                inquiry.getCreatedAt(),
                inquiry.getUpdatedAt()
        );
    }
}
