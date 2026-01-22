package com.example.ssak3.domain.timedeal.model.response;

import com.example.ssak3.common.enums.TimeDealStatus;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static com.example.ssak3.domain.timedeal.utils.TimeDealUtils.formatRemainingTime;

@Getter
@RequiredArgsConstructor
public class TimeDealListGetResponse {
    private final Long id;
    private final String productName;
    private final Integer dealPrice;
    private final TimeDealStatus status;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;
    private final String remainingTime;
    public static TimeDealListGetResponse from(TimeDeal timeDeal) {

        LocalDateTime now = LocalDateTime.now();
        TimeDealStatus status = timeDeal.getStatus();
        String remainingTime = null;

        if (status == TimeDealStatus.READY || status == TimeDealStatus.OPEN) {
            remainingTime = formatRemainingTime(now, timeDeal.getStartAt(), timeDeal.getEndAt());
        }

        return new TimeDealListGetResponse(
                timeDeal.getId(),
                timeDeal.getProduct().getName(),
                timeDeal.getDealPrice(),
                status,
                timeDeal.getStartAt(),
                timeDeal.getEndAt(),
                remainingTime
        );
    }
}