package com.example.ssak3.domain.usercoupon.model.response;

import com.example.ssak3.domain.coupon.entity.Coupon;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
// Jackson 역직렬화를 위한 기본 생성자
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class CouponListForUserGetResponse {

    private final Long id;
    private final String name;
    private final Integer discountValue;
    private final Integer minOrderPrice;
    private final Integer validDays;
    private final LocalDateTime issueEndDate;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static CouponListForUserGetResponse from(Coupon coupon) {
        return new CouponListForUserGetResponse(
                coupon.getId(),
                coupon.getName(),
                coupon.getDiscountValue(),
                coupon.getMinOrderPrice(),
                coupon.getValidDays(),
                coupon.getIssueEndDate(),
                coupon.getCreatedAt(),
                coupon.getUpdatedAt()
        );
    }
}