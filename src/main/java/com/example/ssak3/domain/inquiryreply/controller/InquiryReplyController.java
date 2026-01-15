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
//    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/inquiries/{inquiryId}")
    public ResponseEntity<ApiResponse> createInquiryReplyApi(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long inquiryId, @RequestBody InquiryReplyCreateRequest request) {

        Long adminId = authUser.getId();
        ApiResponse response = ApiResponse.success("문의 답변 생성 성공", inquiryReplyService.createInquiryReply(adminId, inquiryId, request));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 문의 답변 목록 조회 API
     */
//    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse> getInquiryReplyListApi(@PageableDefault(size = 10, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {

        ApiResponse response = ApiResponse.success("문의 답변 목록 조회 성공", inquiryReplyService.getInquiryReplyList(pageable));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 문의 답변 상세 조회 API
     */
//    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{inquiryReplyId}")
    public ResponseEntity<ApiResponse> getInquiryReplyApi(@PathVariable Long inquiryReplyId) {

        ApiResponse response = ApiResponse.success("문의 답변 조회 성공", inquiryReplyService.getInquiryReply(inquiryReplyId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 문의 답변 수정 API
     */
    @PatchMapping("/{inquiryReplyId}")
    public ResponseEntity<ApiResponse> updateInquiryReplyApi(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long inquiryReplyId, @RequestBody InquiryReplyUpdateRequest request) {

        Long adminId = authUser.getId();

        ApiResponse response = ApiResponse.success("문의 답변 수정", inquiryReplyService.updateInquiryReply(adminId, inquiryReplyId, request));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
