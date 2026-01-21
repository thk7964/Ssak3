package com.example.ssak3.domain.product.model.request;

import com.example.ssak3.common.enums.ProductStatus;
import lombok.Getter;

@Getter
public class ProductUpdateStatusRequest {

    private Long productId;
    private ProductStatus status;

}
