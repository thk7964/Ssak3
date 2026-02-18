package com.example.ssak3.domain.product.model.response;

import com.example.ssak3.common.enums.ProductStatus;
import com.example.ssak3.domain.product.entity.Product;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ProductUpdateStatusResponse {

    private final Long id;
    private final Long categoryId;
    private final String name;
    private final ProductStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static ProductUpdateStatusResponse from(Product product) {

        return new ProductUpdateStatusResponse(
                product.getId(),
                product.getCategory().getId(),
                product.getName(),
                product.getStatus(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
