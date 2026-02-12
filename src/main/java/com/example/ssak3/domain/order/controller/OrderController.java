package com.example.ssak3.domain.order.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.common.model.AuthUser;
import com.example.ssak3.domain.order.model.request.OrderCancelRequest;
import com.example.ssak3.domain.order.model.request.OrderCreateFromCartRequest;
import com.example.ssak3.domain.order.model.request.OrderCreateFromProductRequest;
import com.example.ssak3.domain.order.model.request.OrderStatusUpdateRequest;
import com.example.ssak3.domain.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ssak3/orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * 단일 상품 주문
     */
    @PostMapping("/products")
    public ResponseEntity<ApiResponse> createOrderDraftFromProductApi(
            @AuthenticationPrincipal AuthUser user,
            @Valid @RequestBody OrderCreateFromProductRequest request
    ) {

        ApiResponse response = ApiResponse.success("상품 주문이 생성 되었습니다.", orderService.createOrderFromProduct(user.getId(), request));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 장바구니 상품 주문
     */
    @PostMapping("/carts")
    public ResponseEntity<ApiResponse> createOrderFromCartApi(
            @AuthenticationPrincipal AuthUser user,
            @Valid @RequestBody OrderCreateFromCartRequest request
    ) {

        ApiResponse response = ApiResponse.success("장바구니 상품 주문이 생성 되었습니다.", orderService.createOrderFromCart(user.getId(), request));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 주문 상세 조회
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse> getOrderApi(
            @AuthenticationPrincipal AuthUser user,
            @PathVariable("orderId") Long orderId
    ) {

        ApiResponse response = ApiResponse.success("주문 상세 조회 성공했습니다", orderService.getOrder(user.getId(), orderId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 내 주문 목록 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getOrderListApi(
            @AuthenticationPrincipal AuthUser user,
            @PageableDefault Pageable pageable
    ) {
        ApiResponse response = ApiResponse.success("내 주문 목록 조회 성공했습니다.", orderService.getOrderList(user.getId(), pageable));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 주문 취소
     */
    @PatchMapping("cancel")
    public ResponseEntity<ApiResponse> updateOrderCanceledApi(
            @AuthenticationPrincipal AuthUser user,
            @Valid @RequestBody OrderCancelRequest request
    ) {
        ApiResponse response = ApiResponse.success("주문 취소 성공했습니다.", orderService.updateOrderCanceled(user.getId(), request));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 결제 대기 상태에서 주문 취소
     */
    @PatchMapping("/{orderId}/cancel-pending")
    public ResponseEntity<ApiResponse> cancelPendingOrderApi(
            @AuthenticationPrincipal AuthUser user,
            @PathVariable Long orderId
    ) {
        ApiResponse response = ApiResponse.success("결제 대기 주문 취소 성공했습니다.", orderService.cancelPendingOrder(user.getId(), orderId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 주문 재결제
     */
    @PostMapping("/{orderId}/retry-payment")
    public ResponseEntity<ApiResponse> retryPaymentApi(
            @AuthenticationPrincipal AuthUser user,
            @PathVariable Long orderId
    ) {
        ApiResponse response = ApiResponse.success("재결제 성공했습니다.",  orderService.retryPayment(user.getId(), orderId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}