package com.example.ssak3.domain.product.model.response;

import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ProductGetSearchResponse {

    private final Long productId;
    private final Long timeDealId;
    private final Long categoryId;
    private final String name;
    private final String information;
    private final Integer price;
    private final Integer dealPrice;
    private final String imageUrl;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

}
