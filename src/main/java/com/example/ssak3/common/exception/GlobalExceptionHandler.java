package com.example.ssak3.common.exception;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.model.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse> customException(CustomException e) {

        ErrorCode errorCode = e.getErrorCode();

        ApiResponse response = ApiResponse.error(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {

        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        ApiResponse response = ApiResponse.error(message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
