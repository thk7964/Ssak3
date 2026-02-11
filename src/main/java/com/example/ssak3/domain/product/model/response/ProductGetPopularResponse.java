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
    private final String imageUrl;
    private final LocalDateTime createdAt;

    public static ProductGetPopularResponse from(Product product, TimeDeal timeDeal) {

        Long timeDealId = timeDeal != null ? timeDeal.getId() : null;
        Integer dealPrice = timeDeal != null ? timeDeal.getDealPrice() : null;
        String imageUrl = timeDeal != null ? timeDeal.getImage() : product.getImage();

        return new ProductGetPopularResponse(
                product.getId(),
                timeDealId,
                product.getCategory().getId(),
                product.getName(),
                product.getPrice(),
                dealPrice,
                imageUrl,
                product.getCreatedAt()
        );
    }
}
