package com.example.ssak3.domain.timedeal.model.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TimeDealCreateRequest {
    private Long productId;
    private Integer dealPrice;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
