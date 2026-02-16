package com.example.ssak3.domain.orderProduct.entity;

import com.example.ssak3.domain.order.entity.Order;
import com.example.ssak3.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "order_products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false, name = "unit_price")
    private Integer unitPrice;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "cart_product_id")
    private Long cartProductId;

    public OrderProduct(Order order, Product product, Integer unitPrice, Integer quantity, Long cartProductId) {
        this.order = order;
        this.product = product;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.cartProductId = cartProductId;
    }
}
