package com.example.ssak3.domain.product.model.request;

import com.example.ssak3.common.enums.ProductStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductUpdateStatusRequest {

    private Long productId;
    private ProductStatus status;
}
