package com.example.ssak3.domain.timedeal.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
public class TimeDealCreateRequest {

    @NotNull(message = "상품 아이디는 필수 입력 사항입니다.")
    private Long productId;

    @NotNull(message = "세일 가격은 필수 입력 사항입니다.")
    @Positive(message = "할인 가격은 0보다 커야 합니다.")
    private Integer dealPrice;

    @NotNull(message = "startAt은 필수 입력 값이고 yyyy-MM-dd'T'HH:mm:ss 형식으로 입력해야 합니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startAt;

    @NotNull(message = "endAt은 필수 입력 값이고 yyyy-MM-dd'T'HH:mm:ss 형식으로 입력해야 합니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endAt;
}
