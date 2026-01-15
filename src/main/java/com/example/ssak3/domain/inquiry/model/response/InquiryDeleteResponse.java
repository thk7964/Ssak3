package com.example.ssak3.domain.inquiry.model.response;

import com.example.ssak3.domain.inquiry.entity.Inquiry;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class InquiryDeleteResponse {
    private final Long id;
    private final LocalDateTime createdAt;
    private final LocalDateTime deletedAt;

    public static InquiryDeleteResponse from(Inquiry inquiry) {
        return new InquiryDeleteResponse(
                inquiry.getId(),
                inquiry.getCreatedAt(),
                inquiry.getUpdatedAt()
        );
    }
}
