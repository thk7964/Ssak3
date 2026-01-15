package com.example.ssak3.domain.inquiryreply.controller;


import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.common.model.AuthUser;
import com.example.ssak3.domain.inquiryreply.model.request.InquiryReplyCreateRequest;
import com.example.ssak3.domain.inquiryreply.service.InquiryReplyService;
import lombok.RequiredArgsConstructor;
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

}
