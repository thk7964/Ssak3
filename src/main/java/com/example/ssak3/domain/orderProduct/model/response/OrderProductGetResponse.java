package com.example.ssak3.domain.orderProduct.model.response;

import com.example.ssak3.domain.orderProduct.entity.OrderProduct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderProductGetResponse {

    private final Long orderProductId;
    private final Long productId;
    private final String productName;
    private final Integer unitPrice;
    private final Integer quantity;
    private final Integer linePrice;

    public static OrderProductGetResponse from(OrderProduct orderProduct) {

        Integer unitPrice = orderProduct.getUnitPrice();
        Integer quantity = orderProduct.getQuantity();
        Integer linePrice = unitPrice * quantity;

        return new OrderProductGetResponse(
                orderProduct.getId(),
                orderProduct.getProduct().getId(),
                orderProduct.getProduct().getName(),
                unitPrice,
                quantity,
                linePrice
        );
    }

}
