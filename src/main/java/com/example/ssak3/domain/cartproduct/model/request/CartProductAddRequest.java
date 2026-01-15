package com.example.ssak3.domain.cartproduct.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CartProductAddRequest {

    @NotNull
    private Long productId;

    @NotNull
    private int quantity;

}
