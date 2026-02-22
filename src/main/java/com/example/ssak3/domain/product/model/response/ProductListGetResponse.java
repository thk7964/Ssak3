package com.example.ssak3.domain.product.model.response;

import com.example.ssak3.common.enums.ProductStatus;
import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ProductListGetResponse {

    private final Long id;
    private final Long categoryId;
    private final Long timeDealId;
    private final String categoryName;
    private final String name;
    private final Integer price;
    private final Integer dealPrice;
    private final Integer quantity;
    private final ProductStatus status;
    private final Double averageScore;
    private final String imageUrl;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static ProductListGetResponse from(Product product, TimeDeal timeDeal, String productImageUrl, String timeDealImageUrl) {

        Long timeDealId = timeDeal != null ? timeDeal.getId() : null;
        Integer dealPrice = timeDeal != null ? timeDeal.getDealPrice() : null;
        String imageUrl = (timeDeal != null && timeDealImageUrl != null) ? timeDealImageUrl : productImageUrl;

        return new ProductListGetResponse(
                product.getId(),
                product.getCategory().getId(),
                timeDealId,
                product.getCategory().getName(),
                product.getName(),
                product.getPrice(),
                dealPrice,
                product.getQuantity(),
                product.getStatus(),
                product.getAverageScore(),
                imageUrl,
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
