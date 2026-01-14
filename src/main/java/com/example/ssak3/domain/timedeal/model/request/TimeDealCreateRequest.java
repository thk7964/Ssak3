package com.example.ssak3.domain.timedeal.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
public class TimeDealCreateRequest {

    @NotBlank(message = "상품 아이디는 필수 입력 사항입니다.")
    private Long productId;

    @NotBlank(message = "세일 가격은 필수 입력 사항입니다.")
    private Integer dealPrice;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endAt;
}
