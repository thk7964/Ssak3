package com.example.ssak3.common.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
    PRODUCT_ALREADY_EXISTED(HttpStatus.ALREADY_REPORTED, "이미 존재하는 상품명입니다.")
    ;

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
