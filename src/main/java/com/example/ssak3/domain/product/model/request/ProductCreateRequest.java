package com.example.ssak3.domain.product.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {

    @NotNull
    private Long categoryId;

    @NotBlank(message = "상품 이름은 필수입니다.")
    private String name;

    @NotNull(message = "최소 주문 금액은 필수입니다.")
    @PositiveOrZero(message = "상품 가격은 0원 이상이어야 합니다.")
    private Integer price;

    @NotBlank(message = "상세 설명은 필수입니다.")
    private String information;

    @NotNull(message = "총 상품 수량은 필수입니다.")
    @Min(value = 1, message = "최소 1개 이상의 수량을 설정해야 합니다.")
    private Integer quantity;

    private String image;

    private String detailImage;

}
