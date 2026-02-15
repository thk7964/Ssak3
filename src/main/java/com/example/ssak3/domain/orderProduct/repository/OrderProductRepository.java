package com.example.ssak3.domain.orderProduct.repository;

import com.example.ssak3.common.enums.OrderStatus;
import com.example.ssak3.domain.orderProduct.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    List<OrderProduct> findByOrderId(Long orderId);

    boolean existsByOrderUserIdAndProductIdAndOrderStatus(Long orderUserId, Long orderUserId1, OrderStatus status);
}
