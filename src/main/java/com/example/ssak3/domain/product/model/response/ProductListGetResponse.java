package com.example.ssak3.domain.product.model.response;

import com.example.ssak3.domain.product.entity.Product;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ProductListGetResponse {

    private final Long id;
    private final Long categoryId;
    private final String name;
    private final Integer price;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static ProductListGetResponse from(Product product) {
        return new ProductListGetResponse(
                product.getId(),
                product.getCategory().getId(),
                product.getName(),
                product.getPrice(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
