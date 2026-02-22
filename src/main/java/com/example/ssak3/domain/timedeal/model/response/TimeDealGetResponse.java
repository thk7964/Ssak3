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
    private final String imageUrl;
    private final String productImageUrl;
    private final String detailImageUrl;
    private final String productDetailImageUrl;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static TimeDealGetResponse from(TimeDeal timeDeal, String imageUrl, String detailImageUrl, String productImageUrl, String productDetailImageUrl) {

        return new TimeDealGetResponse(
                timeDeal.getId(),
                timeDeal.getProduct().getId(),
                timeDeal.getProduct().getName(),
                timeDeal.getProduct().getInformation(),
                timeDeal.getDealPrice(),
                timeDeal.getStatus(),
                imageUrl,
                productImageUrl,
                detailImageUrl,
                productDetailImageUrl,
                timeDeal.getStartAt(),
                timeDeal.getEndAt(),
                timeDeal.getCreatedAt(),
                timeDeal.getUpdatedAt()
        );
    }
}
