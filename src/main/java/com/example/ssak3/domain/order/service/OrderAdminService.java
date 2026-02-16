package com.example.ssak3.domain.order.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.OrderStatus;
import com.example.ssak3.common.enums.PaymentStatus;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.order.entity.Order;
import com.example.ssak3.domain.order.model.request.OrderStatusUpdateRequest;
import com.example.ssak3.domain.order.model.response.OrderGetResponse;
import com.example.ssak3.domain.order.repository.OrderRepository;
import com.example.ssak3.domain.orderProduct.entity.OrderProduct;
import com.example.ssak3.domain.orderProduct.repository.OrderProductRepository;
import com.example.ssak3.domain.payment.client.TossPaymentClient;
import com.example.ssak3.domain.payment.entity.Payment;
import com.example.ssak3.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderAdminService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final PaymentRepository paymentRepository;
    private final TossPaymentClient tossPaymentClient;
    private final OrderService orderService;

    /**
     * 주문 상태 변경 (Admin)
     */
    @Transactional
    public OrderGetResponse updateOrderStatus(Long orderId, OrderStatusUpdateRequest request) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        if (request.getOrderStatus().equals(OrderStatus.CANCELED)) {
            if (order.getStatus() == OrderStatus.PAYMENT_PENDING) {

                // 재고 롤백
                List<OrderProduct> ops = orderProductRepository.findByOrderId(orderId);
                for (OrderProduct op : ops) {
                    op.getProduct().rollbackQuantity(op.getQuantity());
                }

                // 쿠폰 롤백
                if (order.getUserCoupon() != null) {
                    order.getUserCoupon().rollback();
                }

                order.updateStatus(OrderStatus.CANCELED);

                return OrderGetResponse.from(order, ops);
            }

            if (order.getStatus().equals(OrderStatus.DONE)) {

                Payment payment = paymentRepository.findByOrder(order)
                        .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));

                if (payment.getStatus() == PaymentStatus.CANCELLED) {
                    return OrderGetResponse.from(order, order.getOrderProducts());
                }

                tossPaymentClient.cancel(payment.getPaymentKey(), "관리자 주문 취소");
                orderService.orderCancel(order, payment);

                return OrderGetResponse.from(order, order.getOrderProducts());
            }
        }

        order.updateStatus(request.getOrderStatus());

        List<OrderProduct> orderProductList = orderProductRepository.findByOrderId(order.getId());

        return OrderGetResponse.from(order, orderProductList);
    }
}
