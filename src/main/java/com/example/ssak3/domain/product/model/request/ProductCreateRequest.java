package com.example.ssak3.domain.product.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {

    private Long categoryId;
    private String name;
    private Integer price;
    private String information;
    private Integer quantity;

    private String image;
    private String detailImage;

}
