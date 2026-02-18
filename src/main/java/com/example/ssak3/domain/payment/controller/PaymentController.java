package com.example.ssak3.domain.payment.controller;

import com.example.ssak3.domain.payment.model.request.PaymentConfirmRequest;
import com.example.ssak3.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ssak3/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/confirm")
    public ResponseEntity<Void> confirmPaymentApi(@RequestBody PaymentConfirmRequest request){

        paymentService.confirmPayment(request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
