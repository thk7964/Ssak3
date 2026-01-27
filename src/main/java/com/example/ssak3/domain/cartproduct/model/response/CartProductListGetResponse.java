package com.example.ssak3.domain.cartproduct.model.response;

import com.example.ssak3.common.enums.ProductStatus;
import com.example.ssak3.domain.cartproduct.entity.CartProduct;
import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartProductListGetResponse {

    private final Long cartProductId;
    private final Long productId;
    private final String productName;
    private final int quantity;
    private final long unitPrice; // 개당 가격(타임딜 상품일 경우 타임딜 가격)
    private final long linePrice; // 수량 * 개당 가격
    private final boolean purchasable; // 구매 가능 여부

    private final boolean isTimeDeal; // 타임딜 여부

    public static CartProductListGetResponse from(CartProduct cartProduct, TimeDeal timeDeal) {
        Product product = cartProduct.getProduct();

        boolean isTimeDeal = (timeDeal != null);

        long unitPrice;
        boolean purchasable;

        if (isTimeDeal) {
            unitPrice =timeDeal.getDealPrice();
            purchasable = timeDeal.getProduct().getQuantity() >= cartProduct.getQuantity();
        }
        else {
            boolean forSale = product.getStatus().equals(ProductStatus.FOR_SALE);

            unitPrice = product.getPrice();
            purchasable = forSale
                    && !product.isDeleted()
                    && product.getQuantity() >= cartProduct.getQuantity();
        }

        long linePrice = unitPrice * cartProduct.getQuantity();

        return new CartProductListGetResponse(
                cartProduct.getId(),
                product.getId(),
                product.getName(),
                cartProduct.getQuantity(),
                unitPrice,
                linePrice,
                purchasable,
                isTimeDeal
        );
    }


}
