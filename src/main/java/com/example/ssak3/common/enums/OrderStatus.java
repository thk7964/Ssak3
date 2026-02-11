package com.example.ssak3.common.enums;

public enum OrderStatus {
    CREATED,            // 주문 생성
    DONE,               // 주문 완료(결제 완료)
    CANCELED,           // 주문 취소
    PAYMENT_PENDING,    // 결제 진행 중
    PAYMENT_FAILED      // 결제 실패
}
