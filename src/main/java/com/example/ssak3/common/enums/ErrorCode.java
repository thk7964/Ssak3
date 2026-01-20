package com.example.ssak3.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // User 에러
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 가입된 이메일입니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    PHONE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 가입된 전화번호입니다."),
    UNREGISTERED_USER(HttpStatus.NOT_FOUND, "가입되지 않은 사용자입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    WITHDRAWN_USER(HttpStatus.NOT_FOUND, "탈퇴한 유저입니다."),
    INVALID_USER_ROLE(HttpStatus.BAD_REQUEST, "유효하지 않은 권한입니다."),

    // Product 에러
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
    PRODUCT_NOT_FOR_SALE(HttpStatus.BAD_REQUEST, "판매중인 상품이 아닙니다."),
    PRODUCT_INSUFFICIENT(HttpStatus.BAD_REQUEST, "상품 재고가 부족합니다."),
    INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "유효한 재고값이 아닙니다."),

    // Category 에러
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."),

    // Cart 에러
    CART_PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "장바구니에서 상품을 찾을 수 없습니다."),
    PASSWORD_INCORRECT(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    CART_ACCESS_DENIED(HttpStatus.FORBIDDEN, "내 장바구니가 아닙니다."),
    CART_PRODUCT_LIMIT(HttpStatus.BAD_REQUEST, "장바구니에 담을 수 있는 수량을 초과했습니다."),

    // Order 에러
    ORDER_PRODUCT_IS_NULL(HttpStatus.BAD_REQUEST, "주문할 상품이 선택되지 않았습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."),
    ORDER_INVALID(HttpStatus.BAD_REQUEST, "유효하지 않은 주문입니다"),

    //TimeDeal 에러
    SALE_PRICE_MUST_BE_LOWER_THAN_ORIGINAL_PRICE(HttpStatus.BAD_REQUEST, "할인가는 정가보다 낮아야 합니다."),
    UPDATED_SALE_PRICE_MUST_BE_LOWER_THAN_CURRENT_SALE_PRICE(HttpStatus.BAD_REQUEST, "변경된 할인가는 기존 할인보다 낮아야 합니다."),
    INVALID_TIME_RANGE(HttpStatus.BAD_REQUEST, "시작 시간은 종료 시간보다 늦을 수 없습니다."),
    TIME_DEAL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 세일 상품을 찾을 수 없습니다."),

    // Token 에러
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),

    // Coupon 에러
    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 쿠폰입니다."),
    COUPON_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 지급된 쿠폰입니다."),
    COUPON_ALREADY_DELETED(HttpStatus.CONFLICT, "이미 삭제된 쿠폰입니다."),
    COUPON_INVALID_TIME_RANGE(HttpStatus.BAD_REQUEST, "종료일은 시작일 이전일 수 없습니다. "),
    COUPON_OUT_OF_STOCK(HttpStatus.CONFLICT, "쿠폰 수량이 모두 소진되었습니다."),
    ADMIN_CANNOT_ISSUE_COUPON(HttpStatus.CONFLICT, "관리자는 쿠폰을 발급받을 수 없습니다."),
    COUPON_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "사용 가능한 상태의 쿠폰이 아닙니다."),
    COUPON_EXPIRED(HttpStatus.BAD_REQUEST, "사용 기한이 만료된 쿠폰입니다."),
    FORBIDDEN_COUPON_ACCESS(HttpStatus.FORBIDDEN, "본인의 쿠폰만 사용할 수 있습니다."),
    COUPON_MIN_ORDER_PRICE_NOT_MET(HttpStatus.BAD_REQUEST, "최소 주문 금액을 만족하지 못해 쿠폰을 사용할 수 없습니다."),

    // Inquiry 에러
    INQUIRY_NOT_FOUND(HttpStatus.NOT_FOUND, "문의 내역을 찾을 수 없습니다."),
    NOT_INQUIRY_WRITER(HttpStatus.FORBIDDEN, "내 문의 내역이 아닙니다."),
    INQUIRY_ALREADY_ANSWERED(HttpStatus.BAD_REQUEST, "답변완료된 문의입니다."),
    INQUIRY_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "삭제된 문의입니다."),

    // InquiryReply 에러
    INQUIRY_REPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "문의 답변 내역을 찾을 수 없습니다."),
    INQUIRY_REPLY_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "삭제된 문의 답변입니다.")


    ;

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
