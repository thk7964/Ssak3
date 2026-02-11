package com.example.ssak3.domain.coupon.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CouponUpdateRequest {

    private Integer totalQuantity;
    private LocalDateTime issueEndDate;
    private Integer validDays;
}
