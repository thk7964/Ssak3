package com.example.ssak3.domain.timedeal.model.response;

import com.example.ssak3.common.enums.TimeDealStatus;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
@Getter
@RequiredArgsConstructor
public class TimeDealSummaryResponse {
    private final Long id;
    private final String productName;
    private final Integer dealPrice;
    private final TimeDealStatus status;
    public static TimeDealSummaryResponse from(TimeDeal timeDeal) {
        return new TimeDealSummaryResponse(
                timeDeal.getId(),
                timeDeal.getProduct().getName(),
                timeDeal.getDealPrice(),
                timeDeal.getStatus(LocalDateTime.now())
        );
    }
}