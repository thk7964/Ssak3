package com.example.ssak3.domain.order.entity;

import com.example.ssak3.common.entity.BaseEntity;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.usercoupon.entity.UserCoupon;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_coupon_id")
    private UserCoupon userCoupon;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false, name = "total_price")
    private Long totalPrice;

    @Column(nullable = false)
    private String address;
}
