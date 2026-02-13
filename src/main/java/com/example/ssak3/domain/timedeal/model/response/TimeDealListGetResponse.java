package com.example.ssak3.domain.timedeal.model.response;

import com.example.ssak3.common.enums.TimeDealStatus;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import lombok.*;

import java.time.LocalDateTime;

import static com.example.ssak3.domain.timedeal.utils.TimeDealUtils.formatRemainingTime;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class TimeDealListGetResponse {
    private final Long id;
    private final String productName;
    private final Integer originalPrice;
    private final Integer dealPrice;
    private final TimeDealStatus status;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;
    private final String imageUrl;
    private final String remainingTime;

    public static TimeDealListGetResponse from(TimeDeal timeDeal, String imageUrl) {

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
                imageUrl,
                remainingTime
        );
    }

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
                timeDeal.getProduct().getPrice(),
                timeDeal.getDealPrice(),
                status,
                timeDeal.getStartAt(),
                timeDeal.getEndAt(),
                timeDeal.getImage(),
                remainingTime
        );
    }
}