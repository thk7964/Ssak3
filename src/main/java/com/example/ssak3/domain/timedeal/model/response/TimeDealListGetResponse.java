package com.example.ssak3.domain.timedeal.model.response;

import com.example.ssak3.common.enums.TimeDealStatus;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.example.ssak3.domain.timedeal.utils.TimeDealUtils.formatRemainingTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TimeDealListGetResponse {
    private Long id;
    private String productName;
    private Integer dealPrice;
    private TimeDealStatus status;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String imageUrl;
    private String remainingTime;

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
                timeDeal.getImage(),
                remainingTime
        );
    }
}