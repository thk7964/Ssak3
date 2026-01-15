package com.example.ssak3.domain.cartproduct.model.response;

import com.example.ssak3.domain.cartproduct.entity.CartProduct;
import com.example.ssak3.domain.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartProductListGetResponse {

    private final Long cartProductId;
    private final Long productId;
    private final String productName;
    private final int quantity;
    private final long unitPrice; // 개당 가격
    private final long linePrice; // 수량 * 개당 가격
    private final boolean purchasable; // 구매 가능 여부

    public static CartProductListGetResponse from(CartProduct cartProduct) {
        Product product = cartProduct.getProduct();

        // TODO : enum 사용 시 수정 필요
        boolean purchasable = product.getStatus().equals("FOR_SALE") && (product.getQuantity() >= cartProduct.getQuantity());

        return new CartProductListGetResponse(
                cartProduct.getId(),
                product.getId(),
                product.getName(),
                cartProduct.getQuantity(),
                product.getPrice(),
                product.getPrice() * cartProduct.getQuantity(),
                purchasable
        );
    }


}
