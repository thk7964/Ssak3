package com.example.ssak3.domain.inquiry.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.common.model.AuthUser;
import com.example.ssak3.domain.inquiry.model.request.InquiryCreateRequest;
import com.example.ssak3.domain.inquiry.model.request.InquiryUpdateRequest;
import com.example.ssak3.domain.inquiry.service.InquiryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ssak3/inquiries")
public class InquiryController {

    private final InquiryService inquiryService;

    /**
     * 문의 생성 API
     **/
    @PostMapping
    public ResponseEntity<ApiResponse> createInquiryApi(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody InquiryCreateRequest request) {

        Long userId = authUser.getId();
        ApiResponse response = ApiResponse.success("문의가 정상적으로 등록되었습니다.", inquiryService.createInquiry(userId, request));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 문의 상세 조회 API
     **/
    @GetMapping("/{inquiryId}")
    public ResponseEntity<ApiResponse> getInquiryApi(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long inquiryId) {

        Long userId = authUser.getId();
        ApiResponse response = ApiResponse.success("선택하신 문의가 정상적으로 조회 완료되었습니다.", inquiryService.getInquiry(userId, inquiryId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 문의 수정 API
     **/
    @PatchMapping("/{inquiryId}")
    public ResponseEntity<ApiResponse> updateInquiryApi(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long inquiryId, @RequestBody InquiryUpdateRequest request) {

        Long userId = authUser.getId();
        ApiResponse response = ApiResponse.success("문의가 수정 완료되었습니다.", inquiryService.updateInquiry(userId, inquiryId, request));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 문의 삭제 API
     **/
    @DeleteMapping("/{inquiryId}")
    public ResponseEntity<ApiResponse> deleteInquiryApi(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long inquiryId) {

        Long userId = authUser.getId();
        ApiResponse response = ApiResponse.success("문의가 삭제되었습니다.", inquiryService.deleteInquiry(userId, inquiryId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
