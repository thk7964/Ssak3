package com.example.ssak3.domain.usercoupon.model.response;

import com.example.ssak3.domain.usercoupon.entity.UserCoupon;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserCouponIssueResponse {

    private final Long userCouponId;    // 발급된 회원 쿠폰의 아이디
    private final String couponName;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static UserCouponIssueResponse from(UserCoupon userCoupon) {
        return new UserCouponIssueResponse(
                userCoupon.getId(),
                userCoupon.getCoupon().getName(),
                userCoupon.getCreatedAt(),
                userCoupon.getUpdatedAt()
        );
    }
}
