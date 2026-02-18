package com.example.ssak3.domain.payment.entity;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.PaymentProvider;
import com.example.ssak3.common.enums.PaymentStatus;
import com.example.ssak3.common.exception.CustomException;
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
    private Order order;

    @Column(name = "payment_key", nullable = false, unique = true)
    private String paymentKey;

    @Column(nullable = false, name = "payment_order_id")
    private String paymentOrderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(nullable = false)
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentProvider provider;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    public Payment(Order order, String paymentKey, String paymentOrderId, Long amount, PaymentProvider provider) {

        this.order = order;
        this.paymentKey = paymentKey;
        this.paymentOrderId = paymentOrderId;
        this.amount = amount;
        this.provider = provider;
        this.status = PaymentStatus.READY;
    }

    public void approve() {

        if (this.status == PaymentStatus.SUCCESS) {
            throw new IllegalArgumentException("결제 승인 불가 상태");
        }

        this.status = PaymentStatus.SUCCESS;
        this.approvedAt = LocalDateTime.now();
    }

    public void fail() {

        if (this.status == PaymentStatus.FAILED) {
            return;
        }

        if (this.status == PaymentStatus.SUCCESS) {
            throw new IllegalArgumentException("이미 승인된 결제");
        }

        this.status = PaymentStatus.FAILED;
    }

    public void cancel() {

        if (this.status == PaymentStatus.CANCELLED) {
            return;
        }

        if (this.status != PaymentStatus.SUCCESS) {
            throw new CustomException(ErrorCode.PAYMENT_NOT_CANCELABLE);
        }

        this.status = PaymentStatus.CANCELLED;
    }
}
