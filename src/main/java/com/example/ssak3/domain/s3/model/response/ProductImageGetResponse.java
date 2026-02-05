package com.example.ssak3.domain.s3.model.response;

import com.example.ssak3.domain.product.entity.Product;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ProductImageGetResponse {

    private final Long productId;
    private final String imageUrl;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static ProductImageGetResponse from(Product product) {
        return new ProductImageGetResponse(
                product.getId(),
                product.getImage(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
