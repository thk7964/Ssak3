package com.example.ssak3.domain.inquiry.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.domain.inquiry.model.request.InquiryCreateRequest;
import com.example.ssak3.domain.inquiry.service.InquiryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inquiries")
public class InquiryController {

    private final InquiryService inquiryService;

    /**
     * 문의 생성 API
     **/
    @PostMapping
    public ResponseEntity<ApiResponse> createInquiryApi(@RequestParam Long userId, @Valid @RequestBody InquiryCreateRequest request) {

        ApiResponse response = ApiResponse.success("문의가 정상적으로 등록되었습니다.", inquiryService.createInquiry(userId, request));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
