package com.example.ssak3.domain.order.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.domain.order.model.request.OrderStatusUpdateRequest;
import com.example.ssak3.domain.order.service.OrderAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ssak3")
public class OrderAdminController {

    private final OrderAdminService orderAdminService;

    /**
     * 주문 상태 변경(admin)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin/orders/{orderId}")
    public ResponseEntity<ApiResponse> updateOrderStatusApi(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderStatusUpdateRequest request
    ) {

        ApiResponse response = ApiResponse.success("주문 상태 변경 성공했습니다.", orderAdminService.updateOrderStatus(orderId, request));

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
}
