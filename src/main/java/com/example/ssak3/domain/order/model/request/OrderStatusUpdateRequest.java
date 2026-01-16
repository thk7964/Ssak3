package com.example.ssak3.domain.order.model.request;

import com.example.ssak3.common.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderStatusUpdateRequest {

    @NotNull
    private OrderStatus orderStatus;

}
