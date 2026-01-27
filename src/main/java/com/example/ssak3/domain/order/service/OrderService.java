package com.example.ssak3.domain.order.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.OrderStatus;
import com.example.ssak3.common.enums.ProductStatus;
import com.example.ssak3.common.enums.UserCouponStatus;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.common.model.PageResponse;
import com.example.ssak3.domain.cart.entity.Cart;
import com.example.ssak3.domain.cart.repository.CartRepository;
import com.example.ssak3.domain.cartproduct.entity.CartProduct;
import com.example.ssak3.domain.cartproduct.repository.CartProductRepository;
import com.example.ssak3.domain.order.entity.Order;
import com.example.ssak3.domain.order.model.request.*;
import com.example.ssak3.domain.order.model.response.OrderGetResponse;
import com.example.ssak3.domain.order.model.response.OrderListGetResponse;
import com.example.ssak3.domain.order.model.response.OrderCreateResponse;
import com.example.ssak3.domain.order.repository.OrderRepository;
import com.example.ssak3.domain.orderProduct.entity.OrderProduct;
import com.example.ssak3.domain.orderProduct.repository.OrderProductRepository;
import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.product.repository.ProductRepository;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import com.example.ssak3.domain.timedeal.repository.TimeDealRepository;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.user.repository.UserRepository;
import com.example.ssak3.domain.usercoupon.entity.UserCoupon;
import com.example.ssak3.domain.usercoupon.repository.UserCouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final UserCouponRepository userCouponRepository;
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final TimeDealRepository timeDealRepository;

    /**
     * 상품 페이지에서 단일 상품 구매
     */
    @Transactional
    public OrderCreateResponse createOrderFromProduct(Long userId, OrderCreateFromProductRequest request) {

        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Product product = productRepository.findByIdAndIsDeletedFalse(request.getProductId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        int quantity = request.getQuantity();

        // 구매 가능 여부 확인 후 단위 가격 저장
        int unitPrice = validatePurchasableReturnUnitPrice(product, quantity);

        long subtotal = unitPrice * quantity;

        Order order = new Order(user, request.getAddress(), null, subtotal);
        Order savedOrder = orderRepository.save(order);

        OrderProduct orderProduct = new OrderProduct(savedOrder, product, unitPrice, quantity);
        orderProductRepository.save(orderProduct);

        // 재고 차감
        product.decreaseQuantity(quantity);

        long discount = 0L;
        UserCoupon userCoupon = null;

        if (request.getUserCouponId() != null) {
            userCoupon = userCouponRepository.findByIdAndUserId(request.getUserCouponId(), userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

            if (!userCoupon.getStatus().equals(UserCouponStatus.AVAILABLE)) {
                throw new CustomException(ErrorCode.COUPON_NOT_AVAILABLE);
            }

            if (userCoupon.getCoupon().getMinOrderPrice() <= subtotal) {
                discount = userCoupon.getCoupon().getDiscountValue();
                userCoupon.use();

                savedOrder.applyCoupon(userCoupon, discount);
            }
            else {
                throw new CustomException(ErrorCode.COUPON_MIN_ORDER_PRICE_NOT_MET);
            }
        }

        // 결제
        // 결제 실패 시 롤백 필요

        savedOrder.updateStatus(OrderStatus.DONE);

        return OrderCreateResponse.from(savedOrder, subtotal, discount);

    }

    /**
     * 장바구니에서 여러 상품 주문
     */
    @Transactional
    public OrderCreateResponse createOrderFromCart(Long userId, OrderCreateFromCartRequest request) {

        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Long cartId = request.getCartId();

        List<Long> cartProductIdList = request.getCartProductIdList();
        if (cartProductIdList.isEmpty()) {
            throw new CustomException(ErrorCode.ORDER_PRODUCT_IS_NULL);
        }

        // 내 장바구니인지 확인
        Cart cart = cartRepository.findByIdAndUserId(cartId, user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.CART_ACCESS_DENIED));

        // 주문할 상품 조회
        List<CartProduct> cartProductList = cartProductRepository.findByCartIdAndIdIn(cart.getId(), cartProductIdList);

        // 입력된 아이디 개수와 장바구니에서 찾은 상품의 개수가 같은지 확인
        if(cartProductList.size() != cartProductIdList.size()) {
            throw new CustomException(ErrorCode.CART_PRODUCT_NOT_FOUND);
        }

        long subtotal = 0L;
        List<Integer> unitPriceList = new ArrayList<>();

        for (CartProduct cartProduct : cartProductList) {
            Product product = productRepository.findByIdAndIsDeletedFalse(cartProduct.getProduct().getId())
                    .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

            int quantity = cartProduct.getQuantity();

            // 구매 가능 여부 확인 후 단위 가격 저장
            int unitPrice = validatePurchasableReturnUnitPrice(product, quantity);
            unitPriceList.add(unitPrice);

            subtotal += unitPrice * quantity;
        }

        Order order = new Order(user, request.getAddress(), null, subtotal);
        Order savedOrder = orderRepository.save(order);

        List<OrderProduct> orderProductList = new ArrayList<>();

        for (int i  = 0; i < cartProductList.size(); i++) {
            CartProduct cartProduct = cartProductList.get(i);
            Product product = cartProduct.getProduct();

            int quantity = cartProduct.getQuantity();
            int unitPrice = unitPriceList.get(i);

            OrderProduct orderProduct = new OrderProduct(savedOrder, product, unitPrice, quantity);
            orderProductList.add(orderProduct);

            // 재고 차감
            product.decreaseQuantity(quantity);

        }

        orderProductRepository.saveAll(orderProductList);

        long discount = 0;
        UserCoupon userCoupon = null;

        // 쿠폰 적용 가능한지 확인
        if(request.getUserCouponId() != null){
            userCoupon = userCouponRepository.findByIdAndUserId(request.getUserCouponId(), userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));

            if(!userCoupon.getStatus().equals(UserCouponStatus.AVAILABLE)){
                throw new CustomException(ErrorCode.COUPON_NOT_AVAILABLE);
            }

            if(userCoupon.getCoupon().getMinOrderPrice() <= subtotal){
                // 할인할 가격 계산
                discount = userCoupon.getCoupon().getDiscountValue();
                // 쿠폰을 사용 완료 상태로 변경
                userCoupon.use();

                savedOrder.applyCoupon(userCoupon, discount);
            }
            else{
                throw new CustomException(ErrorCode.COUPON_MIN_ORDER_PRICE_NOT_MET);
            }
        }

        // 결제
        // 결제 실패 시 롤백 필요

        // 주문한 상품 장바구니에서 제거
        cartProductRepository.deleteAll(cartProductList);

        savedOrder.updateStatus(OrderStatus.DONE);

        return OrderCreateResponse.from(savedOrder, subtotal, discount);

    }

    /**
     * 판매중/재고 체크(주문 가능 여부 체크) 후 단위 가격(주문 시의 1개당 가격) 반환
     */
    private int validatePurchasableReturnUnitPrice(Product product, int quantity) {

        // 삭제된 상품인지 확인
        if (product.isDeleted()) {
            throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        // 재고 확인
        if (product.getQuantity() < quantity) {
            throw new CustomException(ErrorCode.PRODUCT_INSUFFICIENT);
        }

        // 판매중인지 확인
        if (product.getStatus().equals(ProductStatus.FOR_SALE)) {
            return product.getPrice();
        }

        // 판매중x and 타임딜 OPEN인지 확인
        TimeDeal timeDeal = timeDealRepository.findOpenTimeDeal(product.getId(), LocalDateTime.now())
                .orElseThrow(() -> new  CustomException(ErrorCode.PRODUCT_NOT_FOR_SALE));

        return timeDeal.getDealPrice();



    }


    /**
     * 주문 상세 조회
     */
    @Transactional(readOnly = true)
    public OrderGetResponse getOrder(Long userId, Long orderId) {

        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        List<OrderProduct> orderProductList = orderProductRepository.findByOrderId(order.getId());

        return OrderGetResponse.from(order, orderProductList);
    }

    /**
     * 내 주문 목록 조회
     */
    @Transactional(readOnly = true)
    public PageResponse<OrderListGetResponse> getOrderList(Long userId, Pageable pageable) {

        // 주문 상태가 CREATED가 아닌 주문만 조회(주문이 완료된 상태여야 함)
        Page<OrderListGetResponse> orderListPage = orderRepository.findAllByUserIdAndStatusNotOrderByCreatedAtDesc(userId, pageable, OrderStatus.CREATED)
                .map(OrderListGetResponse::from);

        return PageResponse.from(orderListPage);

    }

    /**
     * 주문 취소
     */
    @Transactional
    public OrderGetResponse updateOrderCanceled(Long userId, Long orderId) {

        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        // 주문 완료 상태일 때만 취소 가능
        if (!order.getStatus().equals(OrderStatus.DONE)) {
            throw new CustomException(ErrorCode.ORDER_CAN_NOT_BE_CANCELED);
        }
        order.canceled();

        List<OrderProduct> orderProductList = orderProductRepository.findByOrderId(order.getId());

        return OrderGetResponse.from(order, orderProductList);

    }

    /**
     * 주문 상태 변경(admin)
     */
    @Transactional
    public OrderGetResponse updateOrderStatus(Long orderId, OrderStatusUpdateRequest request) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        order.updateStatus(request.getOrderStatus());

        List<OrderProduct> orderProductList = orderProductRepository.findByOrderId(order.getId());

        return OrderGetResponse.from(order, orderProductList);

    }
}
