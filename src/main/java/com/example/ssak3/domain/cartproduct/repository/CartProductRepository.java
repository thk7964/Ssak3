package com.example.ssak3.domain.cartproduct.repository;

import com.example.ssak3.domain.cartproduct.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {

    List<CartProduct> findAllByCartId(Long cartId);

    Optional<CartProduct> findByCartIdAndProductId(Long cartId, Long productId);

    Optional<CartProduct> findByIdAndCartId(Long cartProductId, Long cartId);
}
