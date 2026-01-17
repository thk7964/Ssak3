package com.example.ssak3.domain.usercoupon.entity;

import com.example.ssak3.common.entity.BaseEntity;
import com.example.ssak3.common.enums.UserCouponStatus;
import com.example.ssak3.domain.coupon.entity.Coupon;
import com.example.ssak3.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "user_coupons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Column(nullable = false, name = "expired_at")
    private LocalDateTime expiredAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserCouponStatus status;

    public UserCoupon(User user, Coupon coupon, LocalDateTime expiredAt, UserCouponStatus status) {
        this.user = user;
        this.coupon = coupon;
        this.expiredAt = expiredAt;
        this.status = status;
    }

    // 상태 : 사용 완료
    public void use() {
        this.status = UserCouponStatus.USED;
    }
}
