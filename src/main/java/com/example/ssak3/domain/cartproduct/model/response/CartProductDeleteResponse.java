package com.example.ssak3.domain.cartproduct.model.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
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
