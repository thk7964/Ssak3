package com.example.ssak3.domain.timedeal.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TimeDealCreateRequest {
    private final Long productId;
    private final Integer dealPrice;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;
}
