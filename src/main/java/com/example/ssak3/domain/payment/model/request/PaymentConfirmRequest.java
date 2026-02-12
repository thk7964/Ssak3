package com.example.ssak3.domain.payment.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentConfirmRequest {
    private String orderId;
    private String paymentKey;
    private Long amount;
}
