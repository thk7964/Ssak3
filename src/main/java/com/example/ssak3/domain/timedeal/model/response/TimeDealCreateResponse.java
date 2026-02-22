package com.example.ssak3.domain.timedeal.model.response;

import com.example.ssak3.common.enums.TimeDealStatus;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class TimeDealCreateResponse {

    private final Long id;
    private final String productName;
    private final String productInformation;
    private final Integer dealPrice;
    private final TimeDealStatus status;
    private final String imageUrl;
    private final String detailImageUrl;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static TimeDealCreateResponse from(TimeDeal timeDeal) {

        return new TimeDealCreateResponse(
                timeDeal.getId(),
                timeDeal.getProduct().getName(),
                timeDeal.getProduct().getInformation(),
                timeDeal.getDealPrice(),
                timeDeal.getStatus(),
                timeDeal.getImage(),
                timeDeal.getDetailImage(),
                timeDeal.getStartAt(),
                timeDeal.getEndAt(),
                timeDeal.getCreatedAt(),
                timeDeal.getUpdatedAt()

        );
    }
}
