package com.example.ssak3.domain.cartproduct.model.response;

import com.example.ssak3.common.enums.ProductStatus;
import com.example.ssak3.common.enums.TimeDealStatus;
import com.example.ssak3.domain.cartproduct.entity.CartProduct;
import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CartProductListGetResponse {

    private final Long cartProductId;
    private final Long productId;
    private final String productName;
    private final int quantity;
    private final long unitPrice;
    private final long linePrice;
    private final boolean purchasable;
    private final Long timeDealId;
    private final String imageUrl;

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
        } else {
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
