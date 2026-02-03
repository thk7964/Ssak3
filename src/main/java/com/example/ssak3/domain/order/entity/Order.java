package com.example.ssak3.domain.order.entity;

import com.example.ssak3.common.entity.BaseEntity;
import com.example.ssak3.common.enums.OrderStatus;
import com.example.ssak3.domain.orderProduct.entity.OrderProduct;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.usercoupon.entity.UserCoupon;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, unique = true)
    private String orderNo;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    public Order(User user, String Address, UserCoupon userCoupon, Long totalPrice) {
        this.user = user;
        this.address = Address;
        this.userCoupon = userCoupon;
        this.totalPrice = totalPrice;
        this.orderNo = generateOrderNo();
    }

    public String generateOrderNo() {
        return "Order-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public void applyCoupon(UserCoupon coupon, long discount) {
        this.userCoupon = coupon;
        this.totalPrice -= discount;
    }

    public void updateStatus(OrderStatus orderStatus) {
        this.status = orderStatus;
    }

    public void canceled() {
        this.status = OrderStatus.CANCELED;
    }

}
