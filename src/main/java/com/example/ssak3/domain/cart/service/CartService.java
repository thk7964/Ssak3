package com.example.ssak3.domain.cart.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.TimeDealStatus;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.cart.entity.Cart;
import com.example.ssak3.domain.cart.model.response.CartGetResponse;
import com.example.ssak3.domain.cart.repository.CartRepository;
import com.example.ssak3.domain.cartproduct.entity.CartProduct;
import com.example.ssak3.domain.cartproduct.model.response.CartProductListGetResponse;
import com.example.ssak3.domain.cartproduct.repository.CartProductRepository;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import com.example.ssak3.domain.timedeal.repository.TimeDealRepository;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartProductRepository cartProductRepository;
    private final TimeDealRepository timeDealRepository;

    /**
     * 내 장바구니 불러오기 + 장바구니 없으면 생성
     */
    @Transactional
    public CartGetResponse getMyCart(Long userId){

        Cart cart = getOrCreateCart(userId);

        // 장바구니 상품 조회
        List<CartProduct> cartProductList = cartProductRepository.findAllByCartIdOrderByUpdatedAtDesc(cart.getId());

        // 상품 id
        List<Long> productIdList = cartProductList
                .stream()
                .map(cp -> cp.getProduct().getId())
                .distinct()
                .toList();

        List<TimeDeal> timeDealList = timeDealRepository.findAllByProductIdInAndStatusAndIsDeletedFalse(productIdList, TimeDealStatus.OPEN);

        Map<Long, TimeDeal> timeDealMap = timeDealList
                .stream()
                .collect(Collectors.toMap(
                        td -> td.getProduct().getId(),
                        Function.identity()
                ));

        List<CartProductListGetResponse> productList = cartProductList
                .stream()
                .map(cp -> CartProductListGetResponse.from(
                        cp,
                        timeDealMap.get(cp.getProduct().getId())
                )).toList();

        return CartGetResponse.from(cart, productList);
    }

    /**
     * 장바구니가 있으면 조회 없으면 생성
     */
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
