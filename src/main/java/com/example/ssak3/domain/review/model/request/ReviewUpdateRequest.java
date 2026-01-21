package com.example.ssak3.domain.review.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ReviewUpdateRequest {

    private Long userId;

    private Long productId;

    @Size(min = 10, message = "10자 이상 입력해야 합니다.")
    private String content;

    @Min(value = 1, message = "점수는 1 이상이어야 합니다.")
    @Max(value = 5, message = "점수는 5 이하여야 합니다.")
    private Integer score;

}
