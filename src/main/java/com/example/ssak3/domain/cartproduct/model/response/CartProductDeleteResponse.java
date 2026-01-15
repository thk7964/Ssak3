package com.example.ssak3.domain.cartproduct.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CartProductDeleteResponse {

    private final Long cartProductId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static CartProductDeleteResponse from(Long cartProductId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new CartProductDeleteResponse(
                cartProductId,
                createdAt,
                updatedAt
        );
    }
}
