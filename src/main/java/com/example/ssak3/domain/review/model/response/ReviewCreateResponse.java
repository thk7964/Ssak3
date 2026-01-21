package com.example.ssak3.domain.review.model.response;

import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.review.entity.Review;
import com.example.ssak3.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;


@Getter
@RequiredArgsConstructor
public class ReviewCreateResponse {

    private final Long id;
    private final Long userId;
    private final String nickname;
    private final Long productId;
    private final String content;
    private final Integer score;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static ReviewCreateResponse from(Review review) {
        return new ReviewCreateResponse(
                review.getId(),
                review.getUser().getId(),
                review.getUser().getNickname(),
                review.getProduct().getId(),
                review.getContent(),
                review.getScore(),
                review.getCreatedAt(),
                review.getUpdatedAt()
        );
    }

}
