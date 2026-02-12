package com.example.ssak3.domain.inquiryreply.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.common.model.AuthUser;
import com.example.ssak3.domain.inquiryreply.model.request.InquiryReplyCreateRequest;
import com.example.ssak3.domain.inquiryreply.model.request.InquiryReplyUpdateRequest;
import com.example.ssak3.domain.inquiryreply.service.InquiryReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ssak3/admin/inquiry-replies")
public class InquiryReplyController {

    private final InquiryReplyService inquiryReplyService;

    /**
     * 문의 답변 생성 API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse> createInquiryReplyApi(@AuthenticationPrincipal AuthUser authUser, @RequestBody InquiryReplyCreateRequest request) {

        Long adminId = authUser.getId();
        ApiResponse response = ApiResponse.success("문의 답변 생성 성공", inquiryReplyService.createInquiryReply(adminId, request));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    /**
     * 문의 목록 조회 API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/inquiries")
    public ResponseEntity<ApiResponse> getInquiryListApi(@PageableDefault(size = 10, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {

        ApiResponse response = ApiResponse.success("문의 목록 조회 성공", inquiryReplyService.getInquiryList(pageable));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    /**
     * 관리자용 문의 상세 조회 API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/inquiries/{inquiryId}")
    public ResponseEntity<ApiResponse> getInquiryForAdminApi(@PathVariable Long inquiryId) {

        ApiResponse response = ApiResponse.success("문의 상세 조회 성공", inquiryReplyService.getInquiryForAdmin(inquiryId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    /**
     * 문의 답변 상세 조회 API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{inquiryReplyId}")
    public ResponseEntity<ApiResponse> getInquiryReplyApi(@PathVariable Long inquiryReplyId) {

        ApiResponse response = ApiResponse.success("문의 답변 조회 성공", inquiryReplyService.getInquiryReply(inquiryReplyId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 문의 답변 수정 API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/inquiries/{inquiryId}")
    public ResponseEntity<ApiResponse> updateInquiryReplyApi(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long inquiryId, @RequestBody InquiryReplyUpdateRequest request) {

        Long adminId = authUser.getId();

        ApiResponse response = ApiResponse.success("문의 답변 수정 성공", inquiryReplyService.updateInquiryReply(adminId, inquiryId, request));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 문의 답변 삭제 API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/inquiries/{inquiryId}")
    public ResponseEntity<ApiResponse> deleteInquiryReplyApi(@PathVariable Long inquiryId) {
        
        ApiResponse response = ApiResponse.success("문의 답변 삭제 성공", inquiryReplyService.deleteInquiryReply(inquiryId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
