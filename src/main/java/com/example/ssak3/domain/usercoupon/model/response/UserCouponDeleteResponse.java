package com.example.ssak3.domain.usercoupon.model.response;

import com.example.ssak3.domain.usercoupon.entity.UserCoupon;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserCouponDeleteResponse {

    private final Long userCouponId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static UserCouponDeleteResponse from(UserCoupon userCoupon) {

        return new UserCouponDeleteResponse(
                userCoupon.getId(),
                userCoupon.getCreatedAt(),
                userCoupon.getUpdatedAt()
        );
    }
}
