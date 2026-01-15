package com.example.ssak3.domain.product.model.response;

import com.example.ssak3.domain.product.entity.Product;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ProductGetResponse {

    private final Long id;
    private final String name;
    private final Integer price;
    private final String status;
    private final String information;
    private final Integer quantity;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;


    public static ProductGetResponse form(Product product) {
       return new ProductGetResponse(
               product.getId(),
               product.getName(),
               product.getPrice(),
               product.getStatus(),
               product.getInformation(),
               product.getQuantity(),
               product.getCreatedAt(),
               product.getUpdatedAt()
        );
    }
}
