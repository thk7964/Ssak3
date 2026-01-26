package com.example.ssak3.domain.cartproduct.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.ProductStatus;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.cart.entity.Cart;
import com.example.ssak3.domain.cart.repository.CartRepository;
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

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartProductService {

    private final CartProductRepository cartProductRepository;
    private final ProductRepository productRepository;

    private final CartService cartService;
    private final CartRepository cartRepository;
    private final TimeDealRepository timeDealRepository;

    /**
     * 장바구니에 상품 담기
     */
    @Transactional
    public CartProductListGetResponse addCartProduct(Long userId, CartProductAddRequest request) {

        // 상품 조회
        Product product = productRepository.findByIdAndIsDeletedFalse(request.getProductId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        // 카트 조회(없으면 생성)
        Cart cart = cartService.getOrCreateCart(userId);

        TimeDeal openTimeDeal = timeDealRepository.findOpenTimeDeal(product.getId(), LocalDateTime.now())
                .orElse(null);

        // 타임딜 여부
        boolean isTimeDeal = (openTimeDeal != null);
        // 일반 판매 중 여부
        boolean normalSale = product.getStatus().equals(ProductStatus.FOR_SALE);

        if(!isTimeDeal && !normalSale){
            throw new CustomException(ErrorCode.PRODUCT_NOT_FOR_SALE);
        }
        // 카트에 같은 상품이 담겨있는지 조히
        Optional<CartProduct> cartProductOptional = cartProductRepository.findByCartIdAndProductId(cart.getId(), request.getProductId());

        CartProduct cartProduct;

        // 기존 상품 있음
        if (cartProductOptional.isPresent()) {
            cartProduct = cartProductOptional.get();

            // 수량 누적
            int newQuantity = cartProduct.getQuantity() + request.getQuantity();

            if (newQuantity > product.getQuantity()) {
                throw new CustomException(ErrorCode.PRODUCT_INSUFFICIENT);
            }

            cartProduct.changeQuantity(newQuantity);
        }

        // 기존 상품 없음
        else {

            long count = cartProductRepository.countByCartId(cart.getId());

            // 장바구니에 담을 수 있는 상품 개수 30개로 제한
            if (count >= 30) {
                throw new CustomException(ErrorCode.CART_PRODUCT_LIMIT);
            }

            if (request.getQuantity() > product.getQuantity()) {
                throw new CustomException(ErrorCode.PRODUCT_INSUFFICIENT);
            }

            cartProduct = new CartProduct(cart, product, request.getQuantity());

            cartProductRepository.save(cartProduct);


        }
        return CartProductListGetResponse.from(cartProduct, openTimeDeal);

    }


    /**
     * 장바구니 상품 수량 변경
     */
    @Transactional
    public CartProductListGetResponse updateCartProductQuantity (Long userId, CartProductQuantityUpdateRequest request) {

        int newQuantity = request.getQuantity();

        Cart cart = cartService.getOrCreateCart(userId);

        CartProduct cartProduct = cartProductRepository.findByIdAndCartId(request.getCartProductId(), cart.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.CART_PRODUCT_NOT_FOUND));

        Product product = cartProduct.getProduct();

        TimeDeal openTimeDeal = timeDealRepository.findOpenTimeDeal(product.getId(), LocalDateTime.now())
                .orElse(null);

        boolean isTimeDeal = (openTimeDeal != null);
        boolean normalSale = product.getStatus().equals(ProductStatus.FOR_SALE);

        if (!isTimeDeal && !normalSale){
            throw new CustomException(ErrorCode.PRODUCT_NOT_FOR_SALE);
        }

        // 재고가 있는 만큼만 담을 수 있도록 함
        if (newQuantity > product.getQuantity()) {
            throw new CustomException(ErrorCode.PRODUCT_INSUFFICIENT);
        }

        cartProduct.changeQuantity(newQuantity);

        return CartProductListGetResponse.from(cartProduct, openTimeDeal);
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
