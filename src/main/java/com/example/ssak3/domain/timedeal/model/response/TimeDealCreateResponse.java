package com.example.ssak3.domain.timedeal.model.response;

import com.example.ssak3.domain.timedeal.TimeDealStatus;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TimeDealCreateResponse {
    private final Long id;
    private final Long productId;
    private final TimeDealStatus status;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static TimeDealCreateResponse from (TimeDeal timeDeal){
        return new TimeDealCreateResponse(
                timeDeal.getId(),
                timeDeal.getProduct().getId(),
                timeDeal.getStatus(LocalDateTime.now()),
                timeDeal.getStartAt(),
                timeDeal.getEndAt(),
                timeDeal.getCreatedAt(),
                timeDeal.getUpdatedAt()

        );
    }
}
