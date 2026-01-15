package com.example.ssak3.domain.cart.model.response;

import com.example.ssak3.domain.cart.entity.Cart;
import com.example.ssak3.domain.cartproduct.model.response.CartProductListGetResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CartGetResponse {

    private final Long cartId;
    private final List<CartProductListGetResponse> productList;
    private final long totalPrice;

    public static CartGetResponse from(Cart cart, List<CartProductListGetResponse> productList) {
        long totalPrice = productList
                .stream()
                .filter(CartProductListGetResponse::isPurchasable)
                .mapToLong(CartProductListGetResponse::getLinePrice)
                .sum();

        return new CartGetResponse(cart.getId(), productList, totalPrice);
    }

}
