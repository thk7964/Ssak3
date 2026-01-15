package com.example.ssak3.domain.cartproduct.model.request;

import lombok.Getter;

@Getter
public class CartProductQuantityUpdateRequest {

    private Long cartProductId;
    private int quantity;

}
