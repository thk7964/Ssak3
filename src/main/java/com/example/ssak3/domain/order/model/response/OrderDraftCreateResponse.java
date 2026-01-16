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
public class OrderDraftCreateResponse {

    private final Long orderId;
    private final OrderStatus orderStatus;
    private final Long totalPrice;
    private final List<OrderProductGetResponse> orderProductList;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static OrderDraftCreateResponse from(Order order, List<OrderProduct> orderProductList) {
        List<OrderProductGetResponse> orderProductGetResponseList = orderProductList
                .stream()
                .map(OrderProductGetResponse::from)
                .toList();

        return new OrderDraftCreateResponse(
                order.getId(),
                order.getStatus(),
                order.getTotalPrice(),
                orderProductGetResponseList,
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

}
