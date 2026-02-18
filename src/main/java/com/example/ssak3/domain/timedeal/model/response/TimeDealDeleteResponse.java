package com.example.ssak3.domain.timedeal.model.response;

import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class TimeDealDeleteResponse {

    private final Long id;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static TimeDealDeleteResponse from(TimeDeal timeDeal) {
        
        return new TimeDealDeleteResponse(
                timeDeal.getId(),
                timeDeal.getCreatedAt(),
                timeDeal.getUpdatedAt()

        );
    }
}
