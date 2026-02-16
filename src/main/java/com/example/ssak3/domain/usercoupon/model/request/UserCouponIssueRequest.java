package com.example.ssak3.domain.usercoupon.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserCouponIssueRequest {

    @NotNull(message = "발급 받을 쿠폰 ID는 필수입니다.")
    private Long couponId;
}
