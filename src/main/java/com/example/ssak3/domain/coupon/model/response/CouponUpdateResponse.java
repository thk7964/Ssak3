package com.example.ssak3.domain.coupon.model.response;

import com.example.ssak3.domain.coupon.entity.Coupon;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CouponUpdateResponse {

    private final Long id;
    private final String name;
    private final Integer discountValue;
    private final Integer totalQuantity;
    private final LocalDateTime issueEndDate;
    private final Integer validDays;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static CouponUpdateResponse from(Coupon coupon) {
        return new CouponUpdateResponse(
                coupon.getId(),
                coupon.getName(),
                coupon.getDiscountValue(),
                coupon.getTotalQuantity(),
                coupon.getIssueEndDate(),
                coupon.getValidDays(),
                coupon.getCreatedAt(),
                coupon.getUpdatedAt()
        );
    }
}
