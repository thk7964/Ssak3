package com.example.ssak3.domain.cartproduct.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartProductQuantityUpdateRequest {

    @NotNull
    private Long cartProductId;

    @NotNull
    @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
    private int quantity;

}
