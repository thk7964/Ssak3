package com.example.ssak3.domain.product.model.response;

import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ProductGetPopularResponse {

    private final Long id;
    private final Long timeDealId;
    private final Long categoryId;
    private final String name;
    private final Integer price;
    private final Integer dealPrice;
    private final LocalDateTime createdAt;

    public static ProductGetPopularResponse from(Product product, TimeDeal timeDeal) {
        return new ProductGetPopularResponse(
                product.getId(),
                timeDeal.getId(),
                product.getCategory().getId(),
                product.getName(),
                product.getPrice(),
                timeDeal.getDealPrice(),
                product.getCreatedAt()
        );
    }
}
