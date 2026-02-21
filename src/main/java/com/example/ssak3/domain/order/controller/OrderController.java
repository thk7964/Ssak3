package com.example.ssak3.domain.order.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.common.model.AuthUser;
import com.example.ssak3.domain.order.model.request.OrderCancelRequest;
import com.example.ssak3.domain.order.model.request.OrderCreateFromCartRequest;
import com.example.ssak3.domain.order.model.request.OrderCreateFromProductRequest;
import com.example.ssak3.domain.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ssak3/orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * 단일 상품 주문 API
     */
    @PostMapping("/products")
    public ResponseEntity<ApiResponse> createOrderDraftFromProductApi(@AuthenticationPrincipal AuthUser user, @Valid @RequestBody OrderCreateFromProductRequest request) {

        ApiResponse response = ApiResponse.success("단일 상품 주문에 성공했습니다.", orderService.createOrderFromProduct(user.getId(), request));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 장바구니 상품 주문 API
     */
    @PostMapping("/carts")
    public ResponseEntity<ApiResponse> createOrderFromCartApi(@AuthenticationPrincipal AuthUser user, @Valid @RequestBody OrderCreateFromCartRequest request) {

        ApiResponse response = ApiResponse.success("장바구니 상품 주문에 성공했습니다.", orderService.createOrderFromCart(user.getId(), request));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 주문 상세 조회 API
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse> getOrderApi(@AuthenticationPrincipal AuthUser user, @PathVariable("orderId") Long orderId) {

        ApiResponse response = ApiResponse.success("주문 상세 조회에 성공했습니다", orderService.getOrder(user.getId(), orderId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 내 주문 목록 조회 API
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getOrderListApi(@AuthenticationPrincipal AuthUser user, @PageableDefault Pageable pageable) {

        ApiResponse response = ApiResponse.success("내 주문 목록 조회 성공했습니다.", orderService.getOrderList(user.getId(), pageable));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 주문 취소 API
     */
    @PatchMapping("/cancel")
    public ResponseEntity<ApiResponse> updateOrderCanceledApi(@AuthenticationPrincipal AuthUser user, @Valid @RequestBody OrderCancelRequest request) {

        ApiResponse response = ApiResponse.success("주문 취소에 성공했습니다.", orderService.updateOrderCanceled(user.getId(), request));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 결제 대기 상태에서 주문 취소 API
     */
    @PatchMapping("/{orderId}/cancel-pending")
    public ResponseEntity<ApiResponse> cancelPendingOrderApi(@AuthenticationPrincipal AuthUser user, @PathVariable Long orderId) {

        ApiResponse response = ApiResponse.success("결제 대기 주문 취소에 성공했습니다.", orderService.cancelPendingOrder(user.getId(), orderId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 주문 재결제 API
     */
    @PostMapping("/{orderId}/retry-payment")
    public ResponseEntity<ApiResponse> retryPaymentApi(@AuthenticationPrincipal AuthUser user, @PathVariable Long orderId) {

        ApiResponse response = ApiResponse.success("재결제에 성공했습니다.", orderService.retryPayment(user.getId(), orderId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}