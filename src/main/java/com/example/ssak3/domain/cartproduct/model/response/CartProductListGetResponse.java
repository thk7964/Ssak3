package com.example.ssak3.domain.cartproduct.model.response;

import com.example.ssak3.common.enums.ProductStatus;
import com.example.ssak3.common.enums.TimeDealStatus;
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
    private final Long timeDealId; // 타임딜
    private final String imageUrl; // 상품 대표 이미지

    public static CartProductListGetResponse from(CartProduct cartProduct, TimeDeal timeDeal, String imageUrl) {
        Product product = cartProduct.getProduct();

        long unitPrice;
        boolean purchasable;

        if (timeDeal != null) {
            unitPrice = timeDeal.getDealPrice();
            purchasable = !timeDeal.isDeleted()
                    && timeDeal.getStatus().equals(TimeDealStatus.OPEN)
                    && !product.isDeleted()
                    && product.getQuantity() >= cartProduct.getQuantity();
        }
        else {
            unitPrice = product.getPrice();
            purchasable = product.getStatus().equals(ProductStatus.FOR_SALE)
                    && !product.isDeleted()
                    && product.getQuantity() >= cartProduct.getQuantity();
        }

        long linePrice = unitPrice * cartProduct.getQuantity();
        Long timeDealId = (timeDeal != null) ? timeDeal.getId() : null;

        return new CartProductListGetResponse(
                cartProduct.getId(),
                product.getId(),
                product.getName(),
                cartProduct.getQuantity(),
                unitPrice,
                linePrice,
                purchasable,
                timeDealId,
                imageUrl
        );
    }


}
