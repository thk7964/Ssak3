package com.example.ssak3.domain.product.model.request;

import com.example.ssak3.common.enums.ProductStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductUpdateRequest {

    private Long categoryId;

    private String name;

    @PositiveOrZero(message = "상품 가격은 0원 이상이어야 합니다.")
    private Integer price;

    private ProductStatus status;

    private String information;

    @Min(value = 1, message = "최소 1개 이상의 수량을 설정해야 합니다.")
    private Integer quantity;

    private String image;

    private String detailImage;

}
