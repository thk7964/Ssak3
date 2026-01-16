package com.example.ssak3.domain.order.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderUpdateRequest {

    @NotNull
    private Long orderId;

    @NotBlank
    private String address;

    private Long userCouponId;

}
