package com.example.ssak3.domain.cartproduct.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.ProductStatus;
import com.example.ssak3.common.enums.TimeDealStatus;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.cart.entity.Cart;
import com.example.ssak3.domain.cart.service.CartService;
import com.example.ssak3.domain.cartproduct.entity.CartProduct;
import com.example.ssak3.domain.cartproduct.model.request.CartProductAddRequest;
import com.example.ssak3.domain.cartproduct.model.request.CartProductDeleteRequest;
import com.example.ssak3.domain.cartproduct.model.request.CartProductQuantityUpdateRequest;
import com.example.ssak3.domain.cartproduct.model.response.CartProductDeleteResponse;
import com.example.ssak3.domain.cartproduct.model.response.CartProductListGetResponse;
import com.example.ssak3.domain.cartproduct.repository.CartProductRepository;
import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.product.repository.ProductRepository;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import com.example.ssak3.domain.timedeal.repository.TimeDealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartProductService {

    private final CartProductRepository cartProductRepository;
    private final ProductRepository productRepository;
    private final TimeDealRepository timeDealRepository;
    private final CartService cartService;

    /**
     * 장바구니에 상품 담기
     */
    @Transactional
    public CartProductListGetResponse addCartProduct(Long userId, CartProductAddRequest request) {

        Product product = productRepository.findByIdAndIsDeletedFalse(request.getProductId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        Cart cart = cartService.getOrCreateCart(userId);

        Long timeDealId = request.getTimeDealId();
        boolean isTimeDeal = timeDealId != null;

        TimeDeal timeDeal = null;

        if (isTimeDeal) {
            timeDeal = timeDealRepository.findByIdAndIsDeletedFalse(timeDealId)
                    .orElseThrow(() -> new CustomException(ErrorCode.TIME_DEAL_NOT_FOUND));

            if (!timeDeal.getStatus().equals(TimeDealStatus.OPEN)) {
                throw new CustomException(ErrorCode.TIME_DEAL_INVALID_STATUS);
            }

            if (!timeDeal.getProduct().getId().equals(product.getId())) {
                throw new CustomException(ErrorCode.TIME_DEAL_PRODUCT_MISMATCH);
            }

        } else {
            if (!product.getStatus().equals(ProductStatus.FOR_SALE)) {
                throw new CustomException(ErrorCode.PRODUCT_NOT_FOR_SALE);
            }
        }

        Optional<CartProduct> cartProductOptional = isTimeDeal
                ? cartProductRepository.findByCartIdAndProductIdAndTimeDealId(cart.getId(), product.getId(), timeDealId)
                : cartProductRepository.findByCartIdAndProductIdAndTimeDealIsNull(cart.getId(), product.getId());

        CartProduct cartProduct;

        if (cartProductOptional.isPresent()) {
            cartProduct = cartProductOptional.get();

            int newQuantity = cartProduct.getQuantity() + request.getQuantity();

            if (newQuantity > product.getQuantity()) {
                throw new CustomException(ErrorCode.PRODUCT_INSUFFICIENT);
            }

            cartProduct.changeQuantity(newQuantity);
        } else {
            long count = cartProductRepository.countByCartId(cart.getId());

            if (count >= 30) {
                throw new CustomException(ErrorCode.CART_PRODUCT_LIMIT);
            }

            if (request.getQuantity() > product.getQuantity()) {
                throw new CustomException(ErrorCode.PRODUCT_INSUFFICIENT);
            }

            cartProduct = new CartProduct(cart, product, timeDeal, request.getQuantity());

            cartProductRepository.save(cartProduct);
        }
        return CartProductListGetResponse.from(cartProduct, timeDeal, null);
    }

    /**
     * 장바구니 상품 수량 변경
     */
    @Transactional
    public CartProductListGetResponse updateCartProductQuantity(Long userId, CartProductQuantityUpdateRequest request) {

        int newQuantity = request.getQuantity();

        if (newQuantity <= 0) {
            throw new CustomException(ErrorCode.INVALID_QUANTITY);
        }

        Cart cart = cartService.getOrCreateCart(userId);

        CartProduct cartProduct = cartProductRepository.findByIdAndCartId(request.getCartProductId(), cart.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.CART_PRODUCT_NOT_FOUND));

        Product product = cartProduct.getProduct();

        TimeDeal cartTimeDeal = cartProduct.getTimeDeal();
        TimeDeal timeDeal = null;

        if (cartTimeDeal != null) {
            timeDeal = timeDealRepository.findByIdAndIsDeletedFalse(cartTimeDeal.getId()).orElse(null);

            if (timeDeal == null || timeDeal.getStatus() != TimeDealStatus.OPEN) {
                throw new CustomException(ErrorCode.TIME_DEAL_INVALID_STATUS);
            }
        } else {
            if (product.getStatus() != ProductStatus.FOR_SALE) {
                throw new CustomException(ErrorCode.PRODUCT_NOT_FOR_SALE);
            }
        }

        if (newQuantity > product.getQuantity()) {
            throw new CustomException(ErrorCode.PRODUCT_INSUFFICIENT);
        }

        cartProduct.changeQuantity(newQuantity);

        return CartProductListGetResponse.from(cartProduct, timeDeal, null);
    }

    /**
     * 장바구니 상품 삭제
     */
    @Transactional
    public CartProductDeleteResponse deleteCartProduct(Long userId, CartProductDeleteRequest request) {

        Cart cart = cartService.getOrCreateCart(userId);

        CartProduct cartProduct = cartProductRepository.findByIdAndCartId(request.getCartProductId(), cart.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.CART_PRODUCT_NOT_FOUND));

        CartProductDeleteResponse response = CartProductDeleteResponse.from(cartProduct.getId(), cartProduct.getCreatedAt(), cartProduct.getUpdatedAt());

        cartProductRepository.delete(cartProduct);

        return response;
    }
}
