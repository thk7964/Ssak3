package com.example.ssak3.domain.product.model.response;

import com.example.ssak3.domain.product.entity.Product;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ProductDeleteResponse {

    private final Long id;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static ProductDeleteResponse form(Product product) {
        return new ProductDeleteResponse(
                product.getId(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
