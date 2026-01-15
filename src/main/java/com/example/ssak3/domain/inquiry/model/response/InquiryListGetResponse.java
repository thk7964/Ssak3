package com.example.ssak3.domain.inquiry.model.response;

import com.example.ssak3.domain.inquiry.entity.Inquiry;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class InquiryListGetResponse {
    private final Long id;
    private final Long userId;
    private final String title;
    private final String status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static InquiryListGetResponse from(Inquiry inquiry) {
        return new InquiryListGetResponse(
                inquiry.getId(),
                inquiry.getUser().getId(),
                inquiry.getTitle(),
                inquiry.getStatus(),
                inquiry.getCreatedAt(),
                inquiry.getUpdatedAt()
        );
    }
}
