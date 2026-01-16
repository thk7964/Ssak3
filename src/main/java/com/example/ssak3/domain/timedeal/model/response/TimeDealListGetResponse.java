package com.example.ssak3.domain.timedeal.model.response;

import com.example.ssak3.common.enums.TimeDealStatus;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
@Getter
@RequiredArgsConstructor
public class TimeDealListGetResponse {
    private final Long id;
    private final String productName;
    private final Integer dealPrice;
    private final TimeDealStatus status;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;
    public static TimeDealListGetResponse from(TimeDeal timeDeal) {
        return new TimeDealListGetResponse(
                timeDeal.getId(),
                timeDeal.getProduct().getName(),
                timeDeal.getDealPrice(),
                timeDeal.getStatus(LocalDateTime.now()),
                timeDeal.getStartAt(),
                timeDeal.getEndAt()
        );
    }
}