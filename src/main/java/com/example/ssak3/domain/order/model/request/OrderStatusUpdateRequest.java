package com.example.ssak3.domain.order.model.request;

import com.example.ssak3.common.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderStatusUpdateRequest {

    @NotNull
    private OrderStatus orderStatus;

}
