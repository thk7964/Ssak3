package com.example.ssak3.domain.timedeal.model.response;

import com.example.ssak3.common.enums.TimeDealStatus;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static com.example.ssak3.domain.timedeal.utils.TimeDealUtils.formatRemainingTime;

@Getter
@RequiredArgsConstructor
public class TimeDealGetResponse {
    private final Long id;
    private final Long productId;
    private final String productName;
    private final String productInformation;
    private final Integer dealPrice;
    private final TimeDealStatus status;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final String remainingTime;

    public static TimeDealGetResponse from(TimeDeal timeDeal) {
        LocalDateTime now = LocalDateTime.now();
        TimeDealStatus status = timeDeal.getStatus();
        String remainingTime = null;

        if (status == TimeDealStatus.READY || status== TimeDealStatus.OPEN){
            remainingTime = formatRemainingTime(now, timeDeal.getStartAt(),timeDeal.getEndAt());
        }

        return new TimeDealGetResponse(
                timeDeal.getId(),
                timeDeal.getProduct().getId(),
                timeDeal.getProduct().getName(),
                timeDeal.getProduct().getInformation(),
                timeDeal.getDealPrice(),
                status,
                timeDeal.getStartAt(),
                timeDeal.getEndAt(),
                timeDeal.getCreatedAt(),
                timeDeal.getUpdatedAt(),
                remainingTime
        );
    }
}
