package com.example.ssak3.domain.review.model.response;

import com.example.ssak3.domain.review.entity.Review;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ReviewUpdateResponse {

    private final Long id;
    private final Long userId;
    private final Long productId;
    private final String content;
    private final Integer score;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static ReviewUpdateResponse from(Review review) {

        return new ReviewUpdateResponse(
                review.getId(),
                review.getUser().getId(),
                review.getProduct().getId(),
                review.getContent(),
                review.getScore(),
                review.getCreatedAt(),
                review.getUpdatedAt()
        );
    }
}
