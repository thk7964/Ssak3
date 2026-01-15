package com.example.ssak3.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // User 에러
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 가입된 이메일입니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
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

    //상품
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 상품을 찾을 수 없습니다."),


    //TimeDeal 에러
    INVALID_SALE_PRICE_BELOW_ORIGINAL(HttpStatus.BAD_REQUEST, "세일 가격은 정가보다 낮아야 합니다."),
    INVALID_SALE_PRICE_BELOW_PREVIOUS(HttpStatus.BAD_REQUEST, "수정된 세일 가격은 기존 세일 가격보다 낮아야 합니다."),
    INVALID_TIME_RANGE(HttpStatus.BAD_REQUEST, "시작 시간은 종료 시간보다 늦을 수 없습니다."),
    TIME_DEAL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 세일 상품을 찾을 수 없습니다."),

    // Token 에러
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),

    // Coupon 에러
    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 쿠폰입니다.")
    ;

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
