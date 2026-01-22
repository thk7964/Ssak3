package com.example.ssak3.domain.coupon.entity;

import com.example.ssak3.common.entity.BaseEntity;
import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.coupon.model.request.CouponUpdateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "coupons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, name = "discount_value")
    private Integer discountValue;

    @Column(nullable = false, name = "total_quantity")
    private Integer totalQuantity;

    @Column(nullable = false, name = "issued_quantity")
    private Integer issuedQuantity = 0;

    @Column(nullable = false, name = "issue_start_date")
    private LocalDateTime issueStartDate;

    @Column(nullable = false, name = "issue_end_date")
    private LocalDateTime issueEndDate;

    @Column(nullable = false, name = "min_order_price")
    private Integer minOrderPrice;

    @Column(name = "valid_days")
    private Integer validDays;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted = false;

    public Coupon(String name, Integer discountValue, Integer totalQuantity, LocalDateTime issueStartDate, LocalDateTime issueEndDate, Integer minOrderPrice, Integer validDays) {
        this.name = name;
        this.discountValue = discountValue;
        this.totalQuantity = totalQuantity;
        this.issueStartDate = issueStartDate;
        this.issueEndDate = issueEndDate;
        this.minOrderPrice = minOrderPrice;
        this.validDays = validDays;
    }

    // Update
    public void update(CouponUpdateRequest request) {
        this.totalQuantity = request.getTotalQuantity();
        this.issueEndDate = request.getIssueEndDate();
        this.validDays = request.getValidDays();
    }

    // SoftDelete
    public void delete() {
        this.isDeleted = true;
    }

    // IssuedQuantity (현재까지 발급된 수량) 증가 메소드
    public void increaseIssuedQuantity() {
        if (this.totalQuantity != null && this.issuedQuantity >= this.totalQuantity) {
            throw new CustomException(ErrorCode.COUPON_OUT_OF_STOCK);
        }
        this.issuedQuantity++;
    }

    // IssuedQuantity (현재까지 발급된 수량) 감소 메소드
    public void decreaseIssuedQuantity() {
        if (this.issuedQuantity > 0) {
            this.issuedQuantity--;
        }
    }
}
