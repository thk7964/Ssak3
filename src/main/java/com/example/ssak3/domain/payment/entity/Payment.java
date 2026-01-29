package com.example.ssak3.domain.payment.entity;

import com.example.ssak3.common.enums.PaymentProvider;
import com.example.ssak3.common.enums.PaymentStatus;
import com.example.ssak3.domain.order.entity.Order;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "payments",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "order_id")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;//주문 대상

    @Column(name = "payment_key", nullable = false, unique = true)
    private String paymentKey;// 토스 결제 키

    @Column(nullable = false)
    private String paymentOrderId;// 토스 orderId

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;// 결제 상태

    @Column(nullable = false)
    private Long amount; //결제 금액

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentProvider provider;//결제 PG

    private LocalDateTime approvedAt;//승인 시각


    public Payment(Order order, String paymentKey, String paymentOrderId,Long amount, PaymentProvider provider) {
        this.order = order;
        this.paymentKey = paymentKey;
        this.paymentOrderId =paymentOrderId;
        this.amount = amount;
        this.provider = provider;
        this.status = PaymentStatus.READY;
    }

    public void approve() {
        if (this.status == PaymentStatus.SUCCESS){
            throw  new IllegalArgumentException("결제 승인 불가 상태");
        }
        this.status = PaymentStatus.SUCCESS;
        this.approvedAt = LocalDateTime.now();
    }

    public void fail(){
        if (this.status == PaymentStatus.SUCCESS){
            throw  new IllegalArgumentException("이미 승인된 결제");
        }
        this.status = PaymentStatus.FAILED;
    }
}
