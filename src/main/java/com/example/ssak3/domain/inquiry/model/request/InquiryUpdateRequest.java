package com.example.ssak3.domain.inquiry.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InquiryUpdateRequest {
    @NotBlank(message = "문의 제목은 필수입니다.")
    private String title;
    @NotBlank(message = "문의 내용은 필수입니다.")
    private String content;
}
