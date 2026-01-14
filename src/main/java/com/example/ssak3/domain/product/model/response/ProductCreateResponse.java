package com.example.ssak3.domain.product.model.response;

import com.example.ssak3.domain.category.entity.Category;
import com.example.ssak3.domain.product.entity.Product;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProductCreateResponse {

    private final Long id;
    private final String name;
    private final Integer price;
    private final String status;
    private final String information;
    private final Integer quantity;

    public static ProductCreateResponse from(Product product) {
        return new ProductCreateResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStatus(),
                product.getInformation(),
                product.getQuantity()
        );

    }

}
