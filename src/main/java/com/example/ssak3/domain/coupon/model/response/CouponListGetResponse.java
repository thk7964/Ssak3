package com.example.ssak3.domain.coupon.model.response;

import com.example.ssak3.domain.coupon.entity.Coupon;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CouponListGetResponse {

    private final Long id;
    private final String name;
    private final Integer discountValue;
    private final Integer totalQuantity;
    private final Integer issuedQuantity;
    private final LocalDateTime issueStartDate;
    private final LocalDateTime issueEndDate;
    private final Integer valiDays;
    private final boolean isDeleted;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static CouponListGetResponse from(Coupon coupon) {
        return new CouponListGetResponse(
                coupon.getId(),
                coupon.getName(),
                coupon.getDiscountValue(),
                coupon.getTotalQuantity(),
                coupon.getIssuedQuantity(),
                coupon.getIssueStartDate(),
                coupon.getIssueEndDate(),
                coupon.getValidDays(),
                coupon.isDeleted(),
                coupon.getCreatedAt(),
                coupon.getUpdatedAt()
        );
    }
}
