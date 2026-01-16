package com.example.ssak3.domain.order.model.response;

import com.example.ssak3.common.enums.OrderStatus;
import com.example.ssak3.domain.order.entity.Order;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class OrderUpdateResponse {

    private final Long orderId;
    private final OrderStatus orderStatus;
    private final String address;
    private final Long subtotal; // 쿠폰 적용 전 금액
    private final Long discount; // 쿠폰 할인 금액
    private final Long totalPrice; // 최종 금액
    private final Long userCouponId; // 쿠폰 사용 x시 null
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static OrderUpdateResponse from(Order order, Long subtotal, Long discount) {
        return new OrderUpdateResponse(
                order.getId(),
                order.getStatus(),
                order.getAddress(),
                subtotal,
                discount,
                order.getTotalPrice(),
                order.getUserCoupon() ==  null ? null : order.getUserCoupon().getId(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

}
