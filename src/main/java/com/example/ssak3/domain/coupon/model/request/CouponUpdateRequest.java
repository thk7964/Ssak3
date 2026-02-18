package com.example.ssak3.domain.coupon.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CouponUpdateRequest {

    @Min(value = 1, message = "최소 1개 이상의 수량을 설정해야 합니다.")
    private Integer totalQuantity;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime issueEndDate;

    @Positive(message = "유효 기간은 1일 이상이어야 합니다.")
    private Integer validDays;
}
