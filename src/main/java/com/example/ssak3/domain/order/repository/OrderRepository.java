package com.example.ssak3.domain.order.repository;

import com.example.ssak3.common.enums.OrderStatus;
import com.example.ssak3.domain.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByIdAndUserId(Long id, Long userId);

    Page<Order> findAllByUserIdAndStatusNotOrderByCreatedAtDesc(Long userId, Pageable pageable,  OrderStatus status);

    Optional<Order> findByOrderNo(String orderId);
}
