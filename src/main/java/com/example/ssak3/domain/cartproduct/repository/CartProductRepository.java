package com.example.ssak3.domain.cartproduct.repository;

import com.example.ssak3.domain.cartproduct.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {

    List<CartProduct> findAllByCartId(Long cartId);
}
