package com.example.ssak3.domain.order.entity;

import com.example.ssak3.common.entity.BaseEntity;
import com.example.ssak3.common.enums.OrderStatus;
import com.example.ssak3.domain.coupon.entity.Coupon;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.usercoupon.entity.UserCoupon;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

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
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.CREATED;

    @Column(nullable = false, name = "total_price")
    private Long totalPrice;

    @Column
    private String address;

    // 결제 전 주문서 생성
    public Order(User user, Long totalPrice) {
        this.user = user;
        this.totalPrice = totalPrice;
    }

    public void update(String Address, UserCoupon userCoupon, Long totalPrice) {
        this.address = Address;
        this.userCoupon = userCoupon;
        this.totalPrice = totalPrice;
    }

    public void updateStatus(OrderStatus orderStatus) {
        this.status = orderStatus;
    }

}
