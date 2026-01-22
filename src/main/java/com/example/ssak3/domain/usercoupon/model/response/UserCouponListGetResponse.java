package com.example.ssak3.domain.usercoupon.model.response;

import com.example.ssak3.common.enums.UserCouponStatus;
import com.example.ssak3.domain.usercoupon.entity.UserCoupon;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserCouponListGetResponse {

    private final Long userCouponId;    // 발급된 회원 쿠폰의 아이디
    private final Long couponId;        // 발급한 쿠폰 아이디
    private final String couponName;
    private final Integer discountValue;
    private final LocalDateTime expiredAt;
    private final UserCouponStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static UserCouponListGetResponse from(UserCoupon userCoupon) {
        return new UserCouponListGetResponse(
                userCoupon.getId(),
                userCoupon.getCoupon().getId(),
                userCoupon.getCoupon().getName(),
                userCoupon.getCoupon().getDiscountValue(),
                userCoupon.getExpiredAt(),
                userCoupon.getStatus(),
                userCoupon.getCreatedAt(),
                userCoupon.getUpdatedAt()
        );
    }

}
