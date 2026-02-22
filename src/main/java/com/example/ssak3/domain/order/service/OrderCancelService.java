package com.example.ssak3.domain.order.service;

import com.example.ssak3.domain.order.entity.Order;
import com.example.ssak3.domain.orderProduct.entity.OrderProduct;
import com.example.ssak3.domain.payment.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderCancelService {

    @Transactional
    public void orderCancel(Order order, Payment payment) {

        for (OrderProduct op : order.getOrderProducts()) {
            op.getProduct().rollbackQuantity(op.getQuantity());
        }
        if (order.getUserCoupon() != null) {
            order.getUserCoupon().rollback();
        }

        if (payment != null) {
            payment.cancel();
        }

        order.canceled();
    }
}
