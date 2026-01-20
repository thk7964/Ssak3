package com.example.ssak3.domain.search.model.response;

import com.example.ssak3.common.enums.ProductStatus;
import com.example.ssak3.domain.product.entity.Product;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ProductSearchResponse {

    private final Long id;
    private final String name;
    private final Integer price;
    private final String information;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private static ProductSearchResponse from(Product product) {
        return new ProductSearchResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getInformation(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
