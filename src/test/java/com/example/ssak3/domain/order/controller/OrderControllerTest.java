package com.example.ssak3.domain.order.controller;

import com.example.ssak3.common.enums.OrderStatus;
import com.example.ssak3.common.enums.ProductStatus;
import com.example.ssak3.common.utils.JwtUtil;
import com.example.ssak3.domain.cart.entity.Cart;
import com.example.ssak3.domain.cart.repository.CartRepository;
import com.example.ssak3.domain.cartproduct.entity.CartProduct;
import com.example.ssak3.domain.cartproduct.repository.CartProductRepository;
import com.example.ssak3.domain.category.entity.Category;
import com.example.ssak3.domain.category.repository.CategoryRepository;
import com.example.ssak3.domain.order.entity.Order;
import com.example.ssak3.domain.order.model.request.OrderCreateFromCartRequest;
import com.example.ssak3.domain.order.model.request.OrderCreateFromProductRequest;
import com.example.ssak3.domain.order.repository.OrderRepository;
import com.example.ssak3.domain.orderProduct.entity.OrderProduct;
import com.example.ssak3.domain.orderProduct.repository.OrderProductRepository;
import com.example.ssak3.domain.payment.repository.PaymentRepository;
import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.product.repository.ProductRepository;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartProductRepository cartProductRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private User user;
    private String token;

    @BeforeEach
    void setup() {
        orderProductRepository.deleteAllInBatch();
        paymentRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        cartProductRepository.deleteAllInBatch();
        cartRepository.deleteAllInBatch();

        user = new User(
                "testUser", "userNick", "test8@test.com", "Aa12345678!!",
                LocalDate.of(2026, 2, 2), "010-0008-0000", "서울특별시"
        );
        userRepository.save(user);
        token = jwtUtil.createToken(user.getId(), user.getEmail(), user.getRole());
    }

    @Test
    @DisplayName("단일 상품 주문 생성 성공 -> 주문/주문상품 저장 + 재고 차감 + PAYMENT_PENDING + 결제 URL")
    void createOrderFromProduct_success() throws Exception {
        // Given
        Category category = new Category("test");
        categoryRepository.save(category);

        Product product = new Product(category, "테스트 상품명", 10000,
                ProductStatus.FOR_SALE, "설명", 10, null, null);
        productRepository.save(product);

        OrderCreateFromProductRequest request =
                new OrderCreateFromProductRequest(product.getId(), 2, "서울시", null);

        // When / Then
        mockMvc.perform(
                        post("/ssak3/orders/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", token)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.subtotal").value(20000))
                .andExpect(jsonPath("$.data.deliveryFee").value(3000))
                .andExpect(jsonPath("$.data.discount").value(0))
                .andExpect(jsonPath("$.data.paymentUrl").isNotEmpty());

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
    @DisplayName("장바구니 주문 생성 성공 -> 주문/주문상품 저장 + 재고 차감 + PAYMENT_PENDING + 결제 URL")
    void createOrderFromCart_success() throws Exception {
        // Given
        Category category = new Category("test");
        categoryRepository.save(category);

        Product p1 = new Product(category, "상품1", 10000, ProductStatus.FOR_SALE, "설명", 10, null, null);
        Product p2 = new Product(category, "상품2", 15000, ProductStatus.FOR_SALE, "설명", 5, null, null);
        productRepository.saveAll(List.of(p1, p2));

        Cart cart = new Cart(user);
        cartRepository.save(cart);

        CartProduct cp1 = new CartProduct(cart, p1, null, 2);
        CartProduct cp2 = new CartProduct(cart, p2, null, 1);
        cartProductRepository.saveAll(List.of(cp1, cp2));

        OrderCreateFromCartRequest request =
                new OrderCreateFromCartRequest(
                        cart.getId(),
                        List.of(cp1.getId(), cp2.getId()),
                        "서울시",
                        null
                );

        // When / Then
        mockMvc.perform(
                        post("/ssak3/orders/carts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", token)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.subtotal").value(35000))
                .andExpect(jsonPath("$.data.deliveryFee").value(0))
                .andExpect(jsonPath("$.data.discount").value(0))
                .andExpect(jsonPath("$.data.paymentUrl").isNotEmpty());

        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(1);

        Order savedOrder = orders.get(0);
        assertThat(savedOrder.getUser().getId()).isEqualTo(user.getId());
        assertThat(savedOrder.getStatus()).isEqualTo(OrderStatus.PAYMENT_PENDING);
        assertThat(savedOrder.getTotalPrice()).isEqualTo(35000);

        List<OrderProduct> ops = orderProductRepository.findByOrderId(savedOrder.getId());
        assertThat(ops).hasSize(2);

        Product reloaded1 = productRepository.findById(p1.getId()).orElseThrow();
        Product reloaded2 = productRepository.findById(p2.getId()).orElseThrow();
        assertThat(reloaded1.getQuantity()).isEqualTo(8); // 10 - 2
        assertThat(reloaded2.getQuantity()).isEqualTo(4); // 5 - 1
    }
}