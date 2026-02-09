package com.example.ssak3.domain.order.model.response;

import com.example.ssak3.common.enums.OrderStatus;
import com.example.ssak3.domain.order.entity.Order;
import com.example.ssak3.domain.orderProduct.entity.OrderProduct;
import com.example.ssak3.domain.orderProduct.model.response.OrderProductGetResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class OrderGetResponse {

    private final Long orderId;
    private final OrderStatus orderStatus;
    private final String address;
    private final Long totalPrice;
    private final Long userCouponId;
    private final List<OrderProductGetResponse> orderProductList;
    private final String orderNo; //결제시 사용
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static OrderGetResponse from(Order order, List<OrderProduct> orderProductList) {
        List<OrderProductGetResponse> list = orderProductList
                .stream()
                .map(OrderProductGetResponse::from)
                .toList();

        return new OrderGetResponse(
                order.getId(),
                order.getStatus(),
                order.getAddress(),
                order.getTotalPrice(),
                order.getUserCoupon() == null ? null : order.getUserCoupon().getId(),
                list,
                order.getOrderNo(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
