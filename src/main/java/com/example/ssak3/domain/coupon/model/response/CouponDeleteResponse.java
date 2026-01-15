package com.example.ssak3.domain.coupon.model.response;

import com.example.ssak3.domain.coupon.entity.Coupon;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CouponDeleteResponse {

    private final Long id;
    private final String name;

    public static CouponDeleteResponse from(Coupon coupon) {
        return new CouponDeleteResponse(
                coupon.getId(),
                coupon.getName()
        );
    }
}
