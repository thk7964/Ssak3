package com.example.ssak3.domain.timedeal.model.response;

import com.example.ssak3.common.enums.TimeDealStatus;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

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

    public static TimeDealListGetResponse from(TimeDeal timeDeal) {

        return new TimeDealListGetResponse(
                timeDeal.getId(),
                timeDeal.getProduct().getName(),
                timeDeal.getProduct().getPrice(),
                timeDeal.getDealPrice(),
                timeDeal.getStatus(),
                timeDeal.getStartAt(),
                timeDeal.getEndAt(),
                timeDeal.getImage()
        );
    }
}