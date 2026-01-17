package com.example.ssak3.domain.order.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.common.model.AuthUser;
import com.example.ssak3.domain.order.model.request.OrderDraftCreateFromCartRequest;
import com.example.ssak3.domain.order.model.request.OrderDraftCreateFromProductRequest;
import com.example.ssak3.domain.order.model.request.OrderStatusUpdateRequest;
import com.example.ssak3.domain.order.model.request.OrderUpdateRequest;
import com.example.ssak3.domain.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ssak3/orders")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    /**
     * 단일 상품 주문서 생성
     */
    @PostMapping("/products")
    public ResponseEntity<ApiResponse> createOrderDraftFromProductApi(
            @AuthenticationPrincipal AuthUser user,
            @Valid @RequestBody OrderDraftCreateFromProductRequest request
    ) {

        ApiResponse response = ApiResponse.success("상품 주문서가 생성되었습니다.", orderService.createOrderDraftFromProduct(user.getId(), request));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 장바구니 상품 주문서 생성
     */
    @PostMapping("/carts")
    public ResponseEntity<ApiResponse> createOrderDraftFromCartApi(
            @AuthenticationPrincipal AuthUser user,
            @Valid @RequestBody OrderDraftCreateFromCartRequest request
    ) {

        ApiResponse response = ApiResponse.success("장바구니 상품 주문서가 생성되었습니다.", orderService.createOrderDraftFromCart(user.getId(), request));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 주문서 작성 후 주문 확정
     */
    @PatchMapping
    public ResponseEntity<ApiResponse> updateOrderApi(
            @AuthenticationPrincipal AuthUser user,
            @Valid @RequestBody OrderUpdateRequest request
    ) {

        ApiResponse response = ApiResponse.success("주문이 완료되었습니다.", orderService.updateOrder(user.getId(), request));

        return ResponseEntity.status(HttpStatus.OK).body(response);
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
        ApiResponse response = ApiResponse.success("내 주문 목록 조회 성공했습니다.",  orderService.getOrderList(user.getId(), pageable));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 주문 상태 변경
     */
    @PatchMapping("/{orderId}")
    public ResponseEntity<ApiResponse> updateOrderStatusApi(
            @AuthenticationPrincipal AuthUser user,
            @PathVariable Long orderId,
            @Valid @RequestBody OrderStatusUpdateRequest request
    ) {
        ApiResponse response = ApiResponse.success("주문 상태 변경 성공했습니다.",  orderService.updateOrderStatus(user.getId(), orderId, request));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
