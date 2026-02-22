package com.example.ssak3.domain.order.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.OrderStatus;
import com.example.ssak3.common.enums.ProductStatus;
import com.example.ssak3.common.enums.UserCouponStatus;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.cart.entity.Cart;
import com.example.ssak3.domain.cart.repository.CartRepository;
import com.example.ssak3.domain.cartproduct.entity.CartProduct;
import com.example.ssak3.domain.cartproduct.repository.CartProductRepository;
import com.example.ssak3.domain.category.entity.Category;
import com.example.ssak3.domain.category.repository.CategoryRepository;
import com.example.ssak3.domain.coupon.entity.Coupon;
import com.example.ssak3.domain.coupon.repository.CouponRepository;
import com.example.ssak3.domain.order.entity.Order;
import com.example.ssak3.domain.order.model.request.OrderCreateFromCartRequest;
import com.example.ssak3.domain.order.model.request.OrderCreateFromProductRequest;
import com.example.ssak3.domain.order.model.response.OrderCreateResponse;
import com.example.ssak3.domain.order.repository.OrderRepository;
import com.example.ssak3.domain.orderProduct.entity.OrderProduct;
import com.example.ssak3.domain.orderProduct.repository.OrderProductRepository;
import com.example.ssak3.domain.payment.repository.PaymentRepository;
import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.product.repository.ProductRepository;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.user.repository.UserRepository;
import com.example.ssak3.domain.usercoupon.entity.UserCoupon;
import com.example.ssak3.domain.usercoupon.repository.UserCouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartProductRepository cartProductRepository;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private UserCouponRepository userCouponRepository;

    @BeforeEach
    void clean() {
        orderProductRepository.deleteAllInBatch();
        paymentRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        cartProductRepository.deleteAllInBatch();
        cartRepository.deleteAllInBatch();
        userCouponRepository.deleteAllInBatch();
        couponRepository.deleteAllInBatch();
    }


    @Test
    @DisplayName("단일 상품 주문 통합 테스트 - 주문/주문상품 저장 + 재고 차감 + 결제대기 상태")
    void createOrderFromProduct_통합테스트_paymentPending() {
        // Given
        User user = new User("test3", "user3", "test3@test.com", "Aa12345678!!", LocalDate.of(2026, 2, 2), "010-0003-0000", "서울특별시 노원구");
        userRepository.save(user);

        Category category = new Category("test");
        categoryRepository.save(category);
        Product product = new Product(category, "테스트 상품명", 10000, ProductStatus.FOR_SALE, "설명", 10, null, null);
        productRepository.save(product);

        OrderCreateFromProductRequest request = new OrderCreateFromProductRequest(
                product.getId(),
                2,
                "서울시",
                null
        );

        // When
        OrderCreateResponse response = orderService.createOrderFromProduct(user.getId(), request);

        // Then
        assertThat(response.getSubtotal()).isEqualTo(20000);
        assertThat(response.getDeliveryFee()).isEqualTo(3000);
        assertThat(response.getDiscount()).isEqualTo(0);
        assertThat(response.getPaymentUrl()).isNotBlank();

        // Then
        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(1);

        Order savedOrder = orders.get(0);
        assertThat(savedOrder.getUser().getId()).isEqualTo(user.getId());
        assertThat(savedOrder.getStatus()).isEqualTo(OrderStatus.PAYMENT_PENDING);
        assertThat(savedOrder.getTotalPrice()).isEqualTo(23000);

        List<OrderProduct> ops = orderProductRepository.findByOrderId(savedOrder.getId());
        assertThat(ops).hasSize(1);
        assertThat(ops.get(0).getProduct().getId()).isEqualTo(product.getId());
        assertThat(ops.get(0).getUnitPrice()).isEqualTo(10000);
        assertThat(ops.get(0).getQuantity()).isEqualTo(2);

        Product reloaded = productRepository.findById(product.getId()).orElseThrow();
        assertThat(reloaded.getQuantity()).isEqualTo(8);
    }

    @Test
    @DisplayName("장바구니 상품 주문 통합 테스트 - 주문/주문상품 저장 + 재고 차감 + 결제대기 상태")
    void createOrderFromCart_통합테스트_paymentPending() {
        // Given: 유저
        User user = new User("test4", "user4", "test4@test.com", "Aa12345678!!",
                LocalDate.of(2026, 2, 2), "010-0004-0000", "서울특별시 노원구");
        userRepository.save(user);

        // Given: 카테고리
        Category category = new Category("test");
        categoryRepository.save(category);

        // Given: 상품 2개
        Product p1 = new Product(category, "상품1", 10000, ProductStatus.FOR_SALE, "설명", 10, null, null);
        Product p2 = new Product(category, "상품2", 15000, ProductStatus.FOR_SALE, "설명", 5, null, null);
        productRepository.save(p1);
        productRepository.save(p2);

        // Given: 장바구니 + 장바구니 상품들
        Cart cart = new Cart(user);
        cartRepository.save(cart);

        CartProduct cp1 = new CartProduct(cart, p1, null, 2);
        CartProduct cp2 = new CartProduct(cart, p2, null, 1);
        cartProductRepository.save(cp1);
        cartProductRepository.save(cp2);

        OrderCreateFromCartRequest request = new OrderCreateFromCartRequest(
                cart.getId(),
                List.of(cp1.getId(), cp2.getId()),
                "서울시",
                null
        );

        // When
        OrderCreateResponse response = orderService.createOrderFromCart(user.getId(), request);

        // Then
        assertThat(response.getSubtotal()).isEqualTo(35000);
        assertThat(response.getDeliveryFee()).isEqualTo(0);
        assertThat(response.getDiscount()).isEqualTo(0);
        assertThat(response.getPaymentUrl()).isNotBlank();

        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(1);

        Order savedOrder = orders.get(0);
        assertThat(savedOrder.getUser().getId()).isEqualTo(user.getId());
        assertThat(savedOrder.getStatus()).isEqualTo(OrderStatus.PAYMENT_PENDING);
        assertThat(savedOrder.getTotalPrice()).isEqualTo(35000);

        List<OrderProduct> ops = orderProductRepository.findByOrderId(savedOrder.getId());
        assertThat(ops).hasSize(2);

        OrderProduct op1 = ops.stream()
                .filter(op -> op.getProduct().getId().equals(p1.getId()))
                .findFirst().orElseThrow();
        OrderProduct op2 = ops.stream()
                .filter(op -> op.getProduct().getId().equals(p2.getId()))
                .findFirst().orElseThrow();

        assertThat(op1.getUnitPrice()).isEqualTo(10000);
        assertThat(op1.getQuantity()).isEqualTo(2);
        assertThat(op2.getUnitPrice()).isEqualTo(15000);
        assertThat(op2.getQuantity()).isEqualTo(1);
        Product reloaded1 = productRepository.findById(p1.getId()).orElseThrow();
        Product reloaded2 = productRepository.findById(p2.getId()).orElseThrow();
        assertThat(reloaded1.getQuantity()).isEqualTo(8); // 10 - 2
        assertThat(reloaded2.getQuantity()).isEqualTo(4); // 5 - 1
        assertThat(op1.getCartProductId()).isEqualTo(cp1.getId());
        assertThat(op2.getCartProductId()).isEqualTo(cp2.getId());
    }

    @Test
    @DisplayName("장바구니 상품 주문 - 상품 재고 부족 예외")
    void createOrderFromCart_통합테스트_상품재고부족() {
        // Given
        User user = new User("test4", "user4", "test4@test.com", "Aa12345678!!", LocalDate.of(2026, 2, 2), "010-0000-0001", "서울");
        userRepository.save(user);

        Category category = new Category("test");
        categoryRepository.save(category);

        Product product = new Product(category, "테스트", 10000, ProductStatus.FOR_SALE, "설명", 1, null, null);
        productRepository.save(product);

        Cart cart = new Cart(user);
        cartRepository.save(cart);

        CartProduct cartProduct = new CartProduct(cart, product, null, 2);
        cartProductRepository.save(cartProduct);

        OrderCreateFromCartRequest request = new OrderCreateFromCartRequest(cart.getId(), List.of(cartProduct.getId()), "서울시", null);

        // When / Then
        assertThatThrownBy(() -> orderService.createOrderFromCart(user.getId(), request))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.PRODUCT_INSUFFICIENT);
    }

    @Test
    @DisplayName("단일 상품 주문 - 쿠폰 최소 금액 미달 예외")
    void createOrderFromProduct_통합테스트_쿠폰최소금액미달() {
        // Given
        User user = new User("test4", "user4", "test4@test.com", "Aa12345678!!", LocalDate.of(2026, 2, 2), "010-0000-0001", "서울");
        userRepository.save(user);

        Category category = new Category("test");
        categoryRepository.save(category);

        Product product = new Product(category, "테스트", 10000, ProductStatus.FOR_SALE, "설명", 1, null, null);
        productRepository.save(product);

        Coupon coupon = new Coupon("테스트 쿠폰", 5000, 100, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1), 30000, 7);
        couponRepository.save(coupon);

        UserCoupon userCoupon = new UserCoupon(user, coupon, LocalDateTime.now().plusDays(coupon.getValidDays()), UserCouponStatus.AVAILABLE);
        userCouponRepository.save(userCoupon);

        OrderCreateFromProductRequest request = new OrderCreateFromProductRequest(product.getId(), 1, "배송지", userCoupon.getId());

        // When / Then
        assertThatThrownBy(() -> orderService.createOrderFromProduct(user.getId(), request))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.COUPON_MIN_ORDER_PRICE_NOT_MET);

        assertThat(orderRepository.findAll()).isEmpty();
        assertThat(userCoupon.getStatus()).isEqualTo(UserCouponStatus.AVAILABLE);
    }

}
