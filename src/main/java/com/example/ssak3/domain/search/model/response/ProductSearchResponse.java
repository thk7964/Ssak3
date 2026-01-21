package com.example.ssak3.domain.search.model.response;

import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ProductSearchResponse {

    private final Long productId;
    private final Long timeDealId;
    private final String name;
    private final Integer price;
    private final String information;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private static ProductSearchResponse from(Product product, TimeDeal timeDeal) {
        return new ProductSearchResponse(
                product.getId(),
                timeDeal.getId(),
                product.getName(),
                product.getPrice(),
                product.getInformation(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
