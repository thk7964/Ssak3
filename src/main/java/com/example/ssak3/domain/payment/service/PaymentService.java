package com.example.ssak3.domain.payment.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.OrderStatus;
import com.example.ssak3.common.enums.PaymentProvider;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.cartproduct.repository.CartProductRepository;
import com.example.ssak3.domain.order.entity.Order;
import com.example.ssak3.domain.order.repository.OrderRepository;
import com.example.ssak3.domain.payment.client.TossPaymentClient;
import com.example.ssak3.domain.payment.entity.Payment;
import com.example.ssak3.domain.payment.model.request.PaymentConfirmRequest;
import com.example.ssak3.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final TossPaymentClient tossPaymentClient;
    private final CartProductRepository cartProductRepository;
    private final PaymentFailedService paymentFailedService;

    @Transactional
    public void confirmPayment(PaymentConfirmRequest request) {
        Order order = orderRepository.findByOrderNo(request.getOrderId())
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        if (order.getStatus() == OrderStatus.DONE) {
            log.info("이미 결제 완료된 주문: {}", order.getOrderNo());
            return;
        }

        if (order.getStatus() != OrderStatus.PAYMENT_PENDING) {
            throw new CustomException(ErrorCode.ORDER_NOT_IN_PAYMENT_PENDING);
        }

        if (!order.getTotalPrice().equals(request.getAmount())) {
            order.updateStatus(OrderStatus.PAYMENT_FAILED);
            throw new CustomException(ErrorCode.PAYMENT_AMOUNT_MISMATCH);
        }

        Payment payment = paymentRepository.findByPaymentKey(request.getPaymentKey())
                .orElseGet(() -> paymentRepository.save(
                        new Payment(
                                order,
                                request.getPaymentKey(),
                                request.getOrderId(),
                                request.getAmount(),
                                PaymentProvider.TOSS

                        )));

        try {
            //결제 승인 요청
            tossPaymentClient.confirm(
                    request.getPaymentKey(),
                    request.getOrderId(),
                    request.getAmount()
            );

            List<Long> cartProductIds = order.getOrderProducts().stream()
                    .map(op -> op.getCartProductId())
                    .filter(Objects::nonNull)
                    .toList();

            cartProductRepository.deletePaidProductsFromCart(order.getUser().getId(), cartProductIds);
            payment.approve();
            order.updateStatus(OrderStatus.DONE);

        } catch (Exception e) {
            log.error("결제 승인 후 처리 실패. orderId={}, paymentKey={}", request.getOrderId(), request.getPaymentKey(), e);
            paymentFailedService.paymentFailed(request.getOrderId(), request.getPaymentKey());

            throw e;
        }
    }
}
