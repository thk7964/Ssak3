package com.example.ssak3.domain.inquiryreply.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.InquiryStatus;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.common.model.PageResponse;
import com.example.ssak3.domain.inquiry.entity.Inquiry;
import com.example.ssak3.domain.inquiry.repository.InquiryRepository;
import com.example.ssak3.domain.inquiryreply.entity.InquiryReply;
import com.example.ssak3.domain.inquiryreply.model.request.InquiryReplyCreateRequest;
import com.example.ssak3.domain.inquiryreply.model.response.InquiryReplyCreateResponse;
import com.example.ssak3.domain.inquiryreply.model.response.InquiryReplyGetResponse;
import com.example.ssak3.domain.inquiryreply.model.response.InquiryReplyListGetResponse;
import com.example.ssak3.domain.inquiryreply.repository.InquiryReplyRepository;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InquiryReplyService {

    private final InquiryReplyRepository inquiryReplyRepository;
    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;

    /**
     * 문의 답변 생성
     */
    @Transactional
    public InquiryReplyCreateResponse createInquiryReply(Long adminId, Long inquiryId, InquiryReplyCreateRequest request) {

        User admin = userRepository.findById(adminId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        Inquiry foundInquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new CustomException(ErrorCode.INQUIRY_NOT_FOUND));

        foundInquiry.validateAnswered();  // 이미 답변완료된 문의인지 검사

        InquiryReply inquiryReply = new InquiryReply(
                admin,
                foundInquiry,
                request.getContent()
        );

        InquiryReply savedInquiryReply = inquiryReplyRepository.save(inquiryReply);

        foundInquiry.updateStatus(InquiryStatus.ANSWERED);

        return InquiryReplyCreateResponse.from(savedInquiryReply);
    }

    /**
     * 문의 답변 목록 조회
     */
    @Transactional(readOnly = true)
    public PageResponse<InquiryReplyListGetResponse> getInquiryReplyList(Pageable pageable) {
        Page<InquiryReplyListGetResponse> inquiryReplyListPage = inquiryReplyRepository
                .findAllByIsDeletedFalse(pageable)  // 관리자는 adminId 상관없이 작성한 모든 답변을 볼 수 있음
                .map(InquiryReplyListGetResponse::from);

        return PageResponse.from(inquiryReplyListPage);
    }

    /**
     * 문의 답변 상세 조회
     */
    @Transactional(readOnly = true)
    public InquiryReplyGetResponse getInquiryReply(Long inquiryReplyId) {

        InquiryReply foundInquiryReply = inquiryReplyRepository.findById(inquiryReplyId)
                .orElseThrow(() -> new CustomException(ErrorCode.INQUIRY_REPLY_NOT_FOUND));

        foundInquiryReply.validateInquiryReplyDeleted();  // 삭제된 문의 답변인지 검증

        return InquiryReplyGetResponse.from(foundInquiryReply);
    }

}
