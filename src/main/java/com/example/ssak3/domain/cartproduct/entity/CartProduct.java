package com.example.ssak3.domain.cartproduct.entity;

import com.example.ssak3.common.entity.BaseEntity;
import com.example.ssak3.domain.cart.entity.Cart;
import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "cart_products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timedeal_id")
    private TimeDeal timedeal;

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }

    public CartProduct(Cart cart, Product product, TimeDeal timeDeal, Integer quantity) {
        this.cart = cart;
        this.product = product;
        this.timedeal = timeDeal;
        this.quantity = quantity;
    }

}
