package com.example.ssak3.domain.inquiryreply.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InquiryReplyUpdateRequest {
    @NotBlank(message = "문의 답변 내용은 필수입니다.")
    private String content;
}
