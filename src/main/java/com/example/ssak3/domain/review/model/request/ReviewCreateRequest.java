package com.example.ssak3.domain.review.model.request;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class ReviewCreateRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Long productId;

    @NotBlank
    @Size(min = 10, message = "10자 이상 입력해야 합니다.")
    private String content;

    @NotNull
    @Min(value = 1, message = "점수는 1 이상이어야 합니다.")
    @Max(value = 5, message = "점수는 5 이하여야 합니다.")
    private Integer score;
}
