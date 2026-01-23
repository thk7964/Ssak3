package com.example.ssak3.domain.product.model.response;

import com.example.ssak3.domain.product.entity.Product;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ProductGetPopularResponse {

    private final Long id;
    private final Long categoryId;
    private final String name;
    private final Integer price;
    private final LocalDateTime createdAt;

    public static ProductGetPopularResponse from(Product product) {
        return new ProductGetPopularResponse(
                product.getId(),
                product.getCategory().getId(),
                product.getName(),
                product.getPrice(),
                product.getCreatedAt()
        );
    }
}
