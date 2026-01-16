package com.example.ssak3.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // User 에러
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 가입된 이메일입니다."),
    UNREGISTERED_USER(HttpStatus.NOT_FOUND, "가입되지 않은 사용자입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),

    // Product 에러
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
    PRODUCT_NOT_FOR_SALE(HttpStatus.BAD_REQUEST, "판매중인 상품이 아닙니다."),
    PRODUCT_INSUFFICIENT(HttpStatus.BAD_REQUEST, "상품 재고가 부족합니다."),
    INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "유효한 재고값이 아닙니다."),

    // Cart 에러
    CART_PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니에서 상품을 찾을 수 없습니다."),
    PASSWORD_INCORRECT(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    // Token 에러
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),

    // Coupon 에러
    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 쿠폰입니다."),
    COUPON_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 지급된 쿠폰입니다."),
    COUPON_OUT_OF_STOCK(HttpStatus.CONFLICT, "쿠폰 수량이 모두 소진되었습니다."),
    COUPON_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "사용 가능한 상태의 쿠폰이 아닙니다."),
    COUPON_EXPIRED(HttpStatus.BAD_REQUEST, "사용 기한이 만료된 쿠폰입니다."),
    FORBIDDEN_COUPON_ACCESS(HttpStatus.FORBIDDEN, "본인의 쿠폰만 사용할 수 있습니다."),
    COUPON_MIN_ORDER_PRICE_NOT_MET(HttpStatus.BAD_REQUEST, "최소 주문 금액을 만족하지 못해 쿠폰을 사용할 수 없습니다."),

    ;

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
