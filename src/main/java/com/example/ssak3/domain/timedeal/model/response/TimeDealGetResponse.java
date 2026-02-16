package com.example.ssak3.domain.timedeal.model.response;

import com.example.ssak3.common.enums.TimeDealStatus;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

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
    private final String imageUrl;
    private final String detailImageUrl;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static TimeDealGetResponse from(TimeDeal timeDeal, String imageUrl, String detailImageUrl) {

        return new TimeDealGetResponse(
                timeDeal.getId(),
                timeDeal.getProduct().getId(),
                timeDeal.getProduct().getName(),
                timeDeal.getProduct().getInformation(),
                timeDeal.getDealPrice(),
                timeDeal.getStatus(),
                timeDeal.getStartAt(),
                timeDeal.getEndAt(),
                imageUrl,
                detailImageUrl,
                timeDeal.getCreatedAt(),
                timeDeal.getUpdatedAt()
        );
    }
}
