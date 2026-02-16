package com.example.ssak3.domain.order.model.response;

import com.example.ssak3.common.enums.OrderStatus;
import com.example.ssak3.domain.order.entity.Order;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class OrderCreateResponse {

    private final Long orderId;
    private final OrderStatus orderStatus;
    private final String address;
    private final Long subtotal;
    private final Long discount;
    private final Long totalPrice;
    private final Long userCouponId;
    private final String orderNo;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final String paymentUrl;
    private final long deliveryFee;

    public static OrderCreateResponse from(Order order, Long subtotal, Long discount, String paymentUrl, long deliveryFee) {

        return new OrderCreateResponse(
                order.getId(),
                order.getStatus(),
                order.getAddress(),
                subtotal,
                discount,
                order.getTotalPrice(),
                order.getUserCoupon() == null ? null : order.getUserCoupon().getId(),
                order.getOrderNo(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                paymentUrl,
                deliveryFee
        );
    }

}
