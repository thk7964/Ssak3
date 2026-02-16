package com.example.ssak3.domain.inquiry.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.common.model.AuthUser;
import com.example.ssak3.common.model.PageResponse;
import com.example.ssak3.domain.inquiry.model.request.InquiryCreateRequest;
import com.example.ssak3.domain.inquiry.model.request.InquiryUpdateRequest;
import com.example.ssak3.domain.inquiry.model.response.InquiryListGetResponse;
import com.example.ssak3.domain.inquiry.service.InquiryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
     */
    @PostMapping
    public ResponseEntity<ApiResponse> createInquiryApi(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody InquiryCreateRequest request) {

        ApiResponse response = ApiResponse.success("문의가 정상적으로 등록되었습니다.", inquiryService.createInquiry(authUser.getId(), request));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 문의 전체 조회 API
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getInquiryListApi(@AuthenticationPrincipal AuthUser authUser, @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        PageResponse<InquiryListGetResponse> pageResponse = inquiryService.getInquiryList(authUser.getId(), pageable);

        ApiResponse response = ApiResponse.success("전체 문의가 정상적으로 조회되었습니다.", pageResponse);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 문의 상세 조회 API
     */
    @GetMapping("/{inquiryId}")
    public ResponseEntity<ApiResponse> getInquiryApi(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long inquiryId) {

        ApiResponse response = ApiResponse.success("선택하신 문의가 정상적으로 조회 완료되었습니다.", inquiryService.getInquiry(authUser.getId(), inquiryId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 문의 수정 API
     */
    @PatchMapping("/{inquiryId}")
    public ResponseEntity<ApiResponse> updateInquiryApi(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long inquiryId, @Valid @RequestBody InquiryUpdateRequest request) {

        ApiResponse response = ApiResponse.success("문의가 수정 완료되었습니다.", inquiryService.updateInquiry(authUser.getId(), inquiryId, request));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 문의 삭제 API
     */
    @DeleteMapping("/{inquiryId}")
    public ResponseEntity<ApiResponse> deleteInquiryApi(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long inquiryId) {

        ApiResponse response = ApiResponse.success("문의가 삭제되었습니다.", inquiryService.deleteInquiry(authUser.getId(), inquiryId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
