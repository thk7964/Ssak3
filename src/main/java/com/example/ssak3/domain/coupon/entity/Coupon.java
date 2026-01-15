package com.example.ssak3.domain.coupon.entity;

import com.example.ssak3.common.entity.BaseEntity;
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

    @Column(name = "total_quantity")
    private Integer totalQuantity;

    @Column(nullable = false, name = "issued_quantity")
    private Integer issuedQuantity;

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
}
