package com.example.ssak3.domain.cart.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.cart.entity.Cart;
import com.example.ssak3.domain.cart.model.response.CartGetResponse;
import com.example.ssak3.domain.cart.repository.CartRepository;
import com.example.ssak3.domain.cartproduct.model.response.CartProductListGetResponse;
import com.example.ssak3.domain.cartproduct.repository.CartProductRepository;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartProductRepository cartProductRepository;

    /**
     * 내 장바구니 불러오기 + 장바구니 없으면 생성
     */
    @Transactional
    public CartGetResponse getMyCart(Long userId){

        Cart cart = getOrCreateCart(userId);

        List<CartProductListGetResponse> productList = cartProductRepository.findAllByCartId(cart.getId())
                .stream()
                .map(CartProductListGetResponse::from)
                .toList();

        return CartGetResponse.from(cart, productList);
    }

    @Transactional
    public Cart getOrCreateCart(Long userId){

        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

                    Cart cart = new Cart(user);
                    return cartRepository.save(cart);
                });

    }
}
