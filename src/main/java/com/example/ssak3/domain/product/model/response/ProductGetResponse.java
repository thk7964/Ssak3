package com.example.ssak3.domain.product.model.response;

import com.example.ssak3.common.enums.ProductStatus;
import com.example.ssak3.domain.product.entity.Product;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ProductGetResponse {

    private final Long id;
    private final Long categoryId;
    private final String name;
    private final Integer price;
    private final ProductStatus status;
    private final String information;
    private final Integer quantity;
    private final Double averageScore;
    private final String imageUrl;
    private final String detailImageUrl;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;


    public static ProductGetResponse from(Product product, String imageUrl, String detailImageUrl) {
       return new ProductGetResponse(
               product.getId(),
               product.getCategory().getId(),
               product.getName(),
               product.getPrice(),
               product.getStatus(),
               product.getInformation(),
               product.getQuantity(),
               product.getAverageScore(),
               imageUrl,
               detailImageUrl,
               product.getCreatedAt(),
               product.getUpdatedAt()
        );
    }
}
