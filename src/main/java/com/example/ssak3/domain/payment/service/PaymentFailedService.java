package com.example.ssak3.domain.payment.service;

import com.example.ssak3.common.enums.OrderStatus;
import com.example.ssak3.domain.order.entity.Order;
import com.example.ssak3.domain.order.repository.OrderRepository;
import com.example.ssak3.domain.orderProduct.entity.OrderProduct;
import com.example.ssak3.domain.payment.entity.Payment;
import com.example.ssak3.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentFailedService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void paymentFailed(String orderId, String paymentKey){

        Order order = orderRepository.findByOrderNo(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 내역 없음"));

        Payment payment = paymentRepository.findByPaymentKey(paymentKey)
                .orElseThrow(() -> new IllegalArgumentException("결제 내역 없음"));

        for (OrderProduct op : order.getOrderProducts()){
            op.getProduct().rollbackQuantity(op.getQuantity());
        }

        if (order.getUserCoupon() != null){
            order.getUserCoupon().rollback();
        }

        payment.fail();
        order.updateStatus(OrderStatus.PAYMENT_FAILED);
    }
}
