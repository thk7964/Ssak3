package com.example.ssak3.domain.s3.model.response;

import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class TimeDealImageGetResponse {
    private final Long productId;
    private final String imageUrl;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;


    public static TimeDealImageGetResponse from(TimeDeal timeDeal) {
        return new TimeDealImageGetResponse(
                timeDeal.getId(),
                timeDeal.getImage(),
                timeDeal.getCreatedAt(),
                timeDeal.getUpdatedAt()
        );
    }
}
