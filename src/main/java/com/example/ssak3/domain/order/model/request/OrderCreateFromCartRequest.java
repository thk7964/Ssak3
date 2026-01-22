package com.example.ssak3.domain.order.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderCreateFromCartRequest {

    @NotNull
    private Long cartId;

    @NotNull
    @Size(min = 1, message = "주문할 상품이 선택되지 않았습니다.")
    private List<Long> cartProductIdList;

    @NotBlank
    private String address;

    private Long userCouponId;

}
