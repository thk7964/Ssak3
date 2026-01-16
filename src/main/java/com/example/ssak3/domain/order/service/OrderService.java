package com.example.ssak3.domain.order.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.OrderStatus;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.common.model.PageResponse;
import com.example.ssak3.domain.cart.entity.Cart;
import com.example.ssak3.domain.cart.repository.CartRepository;
import com.example.ssak3.domain.cartproduct.entity.CartProduct;
import com.example.ssak3.domain.cartproduct.repository.CartProductRepository;
import com.example.ssak3.domain.order.entity.Order;
import com.example.ssak3.domain.order.model.request.OrderDraftCreateFromCartRequest;
import com.example.ssak3.domain.order.model.request.OrderDraftCreateFromProductRequest;
import com.example.ssak3.domain.order.model.request.OrderStatusUpdateRequest;
import com.example.ssak3.domain.order.model.request.OrderUpdateRequest;
import com.example.ssak3.domain.order.model.response.OrderDraftCreateResponse;
import com.example.ssak3.domain.order.model.response.OrderGetResponse;
import com.example.ssak3.domain.order.model.response.OrderListGetResponse;
import com.example.ssak3.domain.order.model.response.OrderUpdateResponse;
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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    //private final UserCouponRepository userCouponRepository;
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final TimeDealRepository timeDealRepository;

    /**
     * 주문서 생성(결제 전)
     * 상품 페이지에서 단일 상품 구매 시
     */
    @Transactional
    public OrderDraftCreateResponse createOrderDraftFromProduct(Long userId, OrderDraftCreateFromProductRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        int quantity = request.getQuantity();

        // 구매 가능 여부 확인 후 단위 가격 저장
        int unitPrice = validatePurchasableReturnUnitPrice(product, quantity);

        // 재고 차감
        product.decreaseQuantity(quantity);
        long subtotal = unitPrice * quantity;

        Order order = new Order(user, subtotal);

        Order savedOrder = orderRepository.save(order);

        OrderProduct orderProduct = new OrderProduct(savedOrder, product, unitPrice, quantity);
        orderProductRepository.save(orderProduct);

        return OrderDraftCreateResponse.from(savedOrder, List.of(orderProduct));

    }

    /**
     * 장바구니에서 여러 상품 주문 -> 주문 초안 생성
     */
    @Transactional
    public OrderDraftCreateResponse createOrderDraftFromCart(Long userId, OrderDraftCreateFromCartRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Long cartId = request.getCartId();

        List<Long> productIdList = request.getProductIdList();
        if (productIdList.isEmpty()) {
            throw new CustomException(ErrorCode.ORDER_PRODUCT_IS_NULL);
        }

        // 내 장바구니인지 확인
        Cart cart = cartRepository.findByIdAndUserId(cartId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.CART_ACCESS_DENIED));

        // 주문 상품 조회
        List<CartProduct> cartProductList = cartProductRepository.findByCartIdAndProductIdIn(cart.getId(), productIdList);

        List<OrderProduct> orderProductList = new ArrayList<>();
        long subtotal = 0L;

        for (CartProduct cartProduct : cartProductList) {
            Product product = cartProduct.getProduct();

            int quantity = cartProduct.getQuantity();

            // 구매 가능 여부 확인 후 단위 가격 저장
            int unitPrice = validatePurchasableReturnUnitPrice(product, quantity);

            product.decreaseQuantity(quantity);

            subtotal += unitPrice * quantity;
        }

        Order order = new Order(user, subtotal);
        Order savedOrder = orderRepository.save(order);

        for (CartProduct cartProduct : cartProductList) {
            Product product = cartProduct.getProduct();
            int quantity = cartProduct.getQuantity();
            int unitPrice = product.getPrice();

            OrderProduct orderProduct = new OrderProduct(savedOrder, product, unitPrice, quantity);
            orderProductList.add(orderProduct);

        }

        orderProductRepository.saveAll(orderProductList);

        // 주문한 상품 장바구니에서 제거
        cartProductRepository.deleteAll(cartProductList);

        return OrderDraftCreateResponse.from(savedOrder, orderProductList);


    }

    /**
     * 주문서 입력 후 주문 완료
     */
    @Transactional
    public OrderUpdateResponse updateOrder(Long userId, OrderUpdateRequest request) {

        Order order = orderRepository.findByIdAndUserId(request.getOrderId(), userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        if (!order.getStatus().equals(OrderStatus.CREATED)) {
            throw new CustomException(ErrorCode.ORDER_INVALID);
        }

        // 쿠폰 적용 전 전체 금액
        long subtotal = order.getTotalPrice();
        long discount = 0;
        UserCoupon coupon = null;

        // 유저 쿠폰 추가 후 주석 해제 예정
/*        if(request.getUserCouponId() != null){
            coupon = userCouponRepository.findById(request.getUserCouponId())
                    .orElseThrow(() -> new CustomException(ErrorCode.));

            discount = coupon.getCoupon().getDiscountValue();
        }*/

        long finalPrice = subtotal - discount;

        order.update(request.getAddress(), coupon, finalPrice);

        // 결제 후 주문 완료 처리

        order.updateStatus(OrderStatus.DONE);

        return OrderUpdateResponse.from(order, subtotal, discount);
    }

    /**
     * 판매중/재고 체크(주문 가능 여부 체크) 후 단위 가격(주문 시의 1개당 가격) 반환
     */
    private int validatePurchasableReturnUnitPrice(Product product, int quantity) {

        // 재고 확인
        if (product.getQuantity() < quantity) {
            throw new CustomException(ErrorCode.PRODUCT_INSUFFICIENT);
        }

        // 판매중인지 확인
        // TODO : product status string->enum 변경 시 수정 필요
        if (product.getStatus().equals("FOR_SALE")) {
            return product.getPrice();
        }

        // 판매중x and 타임딜 OPEN인지 확인
        TimeDeal timeDeal = timeDealRepository.findOpenTimeDeal(product.getId(), now())
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
     * 주문 상태 바꾸기
     */
    @Transactional
    public OrderGetResponse updateOrderStatus(Long userId, Long orderId, OrderStatusUpdateRequest request) {

        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        order.updateStatus(request.getOrderStatus());

        List<OrderProduct> orderProductList = orderProductRepository.findByOrderId(order.getId());

        return OrderGetResponse.from(order, orderProductList);

    }
}
