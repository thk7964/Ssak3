package com.example.ssak3.domain.timedeal.model.request;

import com.example.ssak3.common.serializer.LocalDateTimeHourDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TimeDealUpdateRequest {

    @Positive(message = "할인 가격은 0보다 커야 합니다.")
    private Integer dealPrice;

    @JsonDeserialize(using = LocalDateTimeHourDeserializer.class)
    private LocalDateTime startAt;

    @JsonDeserialize(using = LocalDateTimeHourDeserializer.class)
    private LocalDateTime endAt;

    private String image;
    private String detailImage;
}
