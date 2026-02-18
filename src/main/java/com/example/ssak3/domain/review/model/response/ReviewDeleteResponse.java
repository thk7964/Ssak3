package com.example.ssak3.domain.review.model.response;

import com.example.ssak3.domain.review.entity.Review;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ReviewDeleteResponse {

    private final Long id;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static ReviewDeleteResponse from(Review review) {

        return new ReviewDeleteResponse(
                review.getId(),
                review.getCreatedAt(),
                review.getUpdatedAt()
        );
    }
}
