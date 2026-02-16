package com.example.ssak3.domain.usercoupon.model.response;

import com.example.ssak3.common.enums.UserCouponStatus;
import com.example.ssak3.domain.usercoupon.entity.UserCoupon;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserCouponUseResponse {

    private final Long userCouponId;
    private final String couponName;
    private final UserCouponStatus status;

    public static UserCouponUseResponse from(UserCoupon userCoupon) {

        return new UserCouponUseResponse(
                userCoupon.getId(),
                userCoupon.getCoupon().getName(),
                userCoupon.getStatus()
        );
    }
}
