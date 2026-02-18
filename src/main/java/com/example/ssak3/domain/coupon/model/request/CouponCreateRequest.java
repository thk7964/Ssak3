package com.example.ssak3.domain.coupon.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CouponCreateRequest {

    @NotBlank(message = "쿠폰 이름은 필수입니다.")
    @Size(max = 100, message = "쿠폰 이름은 100자 이내여야 합니다.")
    private String name;

    @NotNull(message = "할인 금액은 필수입니다.")
    @Positive(message = "할인 금액은 0보다 커야 합니다.")
    private Integer discountValue;

    @NotNull(message = "총 발행 수량은 필수입니다.")
    @Min(value = 1, message = "최소 1개 이상의 수량을 설정해야 합니다.")
    private Integer totalQuantity;

    @NotNull(message = "발급 시작일은 필수입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime issueStartDate;

    @NotNull(message = "발급 종료일은 필수입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime issueEndDate;

    @NotNull(message = "최소 주문 금액은 필수입니다.")
    @PositiveOrZero(message = "최소 주문 금액은 0원 이상이어야 합니다.")
    private Integer minOrderPrice;

    @Positive(message = "유효 기간은 1일 이상이어야 합니다.")
    private Integer validDays;
}