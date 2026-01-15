package com.example.ssak3.domain.product.model.request;

import lombok.Getter;

@Getter
public class ProductUpdateRequest {

    private String name;
    private Integer price;
    private String status;
    private String information;
    private Integer quantity;

}
