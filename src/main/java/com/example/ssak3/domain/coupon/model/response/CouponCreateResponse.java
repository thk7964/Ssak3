package com.example.ssak3.domain.coupon.model.response;

import com.example.ssak3.domain.coupon.entity.Coupon;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CouponCreateResponse {

    private final Long id;
    private final String name;
    private final Integer discountValue;
    private final Integer totalQuantity;

    public static CouponCreateResponse from(Coupon coupon) {
        return new CouponCreateResponse(
                coupon.getId(),
                coupon.getName(),
                coupon.getDiscountValue(),
                coupon.getTotalQuantity()
        );
    }
}
