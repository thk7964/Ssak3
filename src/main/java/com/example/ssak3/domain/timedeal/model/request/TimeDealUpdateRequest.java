package com.example.ssak3.domain.timedeal.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TimeDealUpdateRequest {

    @Positive(message = "할인 가격은 0보다 커야 합니다.")
    private Integer dealPrice;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH")
    private LocalDateTime startAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH")
    private LocalDateTime endAt;

    private String image;
    private String detailImage;
}
