package com.example.ssak3.domain.payment.service;

import com.example.ssak3.common.enums.OrderStatus;
import com.example.ssak3.common.enums.PaymentProvider;
import com.example.ssak3.domain.order.entity.Order;
import com.example.ssak3.domain.order.repository.OrderRepository;
import com.example.ssak3.domain.payment.client.TossPaymentClient;
import com.example.ssak3.domain.payment.entity.Payment;
import com.example.ssak3.domain.payment.model.request.PaymentConfirmRequest;
import com.example.ssak3.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final TossPaymentClient tossPaymentClient;

    @Transactional
    public void confirmPayment(PaymentConfirmRequest request) {
        Order order = orderRepository.findByOrderNo(request.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("주문없음"));

        if (order.getStatus() == OrderStatus.DONE) {
            return;
        }

        if (order.getStatus() != OrderStatus.PAYMENT_PENDING) {
            throw new IllegalStateException("결제 진행 중인 주문이 아님");
        }

        if (!order.getTotalPrice().equals(request.getAmount())) {
            order.updateStatus(OrderStatus.PAYMENT_FAILED);
            throw new IllegalArgumentException("결제 금액 불일치");
        }

        Payment payment = paymentRepository.findByPaymentKey(request.getPaymentKey())
                .orElseGet(() -> paymentRepository.save(
                        new Payment(order, request.getPaymentKey(), request.getOrderId(), request.getAmount(), PaymentProvider.TOSS)
                ));


        try {
            //결제 승인 요청
            tossPaymentClient.confirm(
                    request.getPaymentKey(),
                    request.getOrderId(),
                    request.getAmount()
            );

            payment.approve();
            order.updateStatus(OrderStatus.DONE);
            order.decreaseStock();

        } catch (Exception e) {
            payment.fail();
            order.updateStatus(OrderStatus.PAYMENT_FAILED);
            throw e;
        }

    }
}
