package com.example.ssak3.domain.product.model.request;

import com.example.ssak3.common.enums.ProductStatus;
import lombok.Getter;

@Getter
public class ProductUpdateRequest {

    private Long categoryId;
    private String name;
    private Integer price;
    private ProductStatus status;
    private String information;
    private Integer quantity;

}
