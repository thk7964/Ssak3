package com.example.ssak3.domain.cartproduct.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartProductListGetResponse {

    private Long cartProductId;
    private Long productId;
    private String productName;
    private int quantity;
    private long unitPrice; // 개당 가격
    private long linePrice; // 수량 * 개당 가격

    public static CartProductListGetResponse from(Long cartProductId, Long productId, String productName, int quantity, long unitPrice) {
        return new CartProductListGetResponse(
                cartProductId,
                productId,
                productName,
                quantity,
                unitPrice,
                unitPrice * quantity
        );
    }


}
