package com.example.ssak3.domain.coupon.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CouponCreateRequest {

    private String name;
    private Integer discountValue;
    private Integer totalQuantity;
    private LocalDateTime issueStartDate;
    private LocalDateTime issueEndDate;
    private Integer minOrderPrice;
    private Integer validDays;

}
