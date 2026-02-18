package com.example.ssak3.domain.inquiryreply.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.InquiryStatus;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.common.model.PageResponse;
import com.example.ssak3.domain.inquiry.entity.Inquiry;
import com.example.ssak3.domain.inquiry.model.response.InquiryGetResponse;
import com.example.ssak3.domain.inquiry.model.response.InquiryListGetResponse;
import com.example.ssak3.domain.inquiry.repository.InquiryRepository;
import com.example.ssak3.domain.inquiryreply.entity.InquiryReply;
import com.example.ssak3.domain.inquiryreply.model.request.InquiryReplyCreateRequest;
import com.example.ssak3.domain.inquiryreply.model.request.InquiryReplyUpdateRequest;
import com.example.ssak3.domain.inquiryreply.model.response.InquiryReplyCreateResponse;
import com.example.ssak3.domain.inquiryreply.model.response.InquiryReplyDeleteResponse;
import com.example.ssak3.domain.inquiryreply.model.response.InquiryReplyUpdateResponse;
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
    public InquiryReplyCreateResponse createInquiryReply(Long adminId, InquiryReplyCreateRequest request) {

        User admin = userRepository.findByIdAndIsDeletedFalse(adminId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        Inquiry inquiry = inquiryRepository.findByIdAndIsDeletedFalse(request.getInquiryId())
                .orElseThrow(() -> new CustomException(ErrorCode.INQUIRY_NOT_FOUND));

        InquiryReply inquiryReply = new InquiryReply(
                admin,
                inquiry,
                request.getContent()
        );

        InquiryReply savedInquiryReply = inquiryReplyRepository.save(inquiryReply);

        inquiry.updateStatus(InquiryStatus.ANSWERED);

        return InquiryReplyCreateResponse.from(savedInquiryReply);
    }

    /**
     * 관리자용 문의 목록 조회
     */
    @Transactional(readOnly = true)
    public PageResponse<InquiryListGetResponse> getInquiryList(Pageable pageable) {

        Page<InquiryListGetResponse> inquiryListPage = inquiryRepository
                .findAllByIsDeletedFalse(pageable)
                .map(InquiryListGetResponse::from);

        return PageResponse.from(inquiryListPage);
    }

    /**
     * 관리자용 문의 상세 조회
     */
    @Transactional(readOnly = true)
    public InquiryGetResponse getInquiryForAdmin(Long inquiryId) {

        Inquiry foundInquiry = inquiryRepository.findByIdAndIsDeletedFalse(inquiryId)
                .orElseThrow(() -> new CustomException(ErrorCode.INQUIRY_NOT_FOUND));

        InquiryReply inquiryReply = inquiryReplyRepository.findByInquiryIdAndIsDeletedFalse(inquiryId)
                .orElse(null);

        return InquiryGetResponse.from(foundInquiry, inquiryReply);
    }

    /**
     * 문의 답변 수정
     */
    @Transactional
    public InquiryReplyUpdateResponse updateInquiryReply(Long adminId, Long inquiryId, InquiryReplyUpdateRequest request) {

        User admin = userRepository.findById(adminId)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));

        InquiryReply inquiryReply = inquiryReplyRepository
                .findByInquiryIdAndIsDeletedFalse(inquiryId)
                .orElseThrow(() -> new CustomException(ErrorCode.INQUIRY_REPLY_NOT_FOUND));

        inquiryReply.update(admin, request.getContent());

        return InquiryReplyUpdateResponse.from(inquiryReply);
    }

    /**
     * 문의 답변 삭제
     */
    @Transactional
    public InquiryReplyDeleteResponse deleteInquiryReply(Long inquiryId) {

        InquiryReply inquiryReply = inquiryReplyRepository
                .findByInquiryIdAndIsDeletedFalse(inquiryId)
                .orElseThrow(() -> new CustomException(ErrorCode.INQUIRY_REPLY_NOT_FOUND));

        inquiryReply.softDelete();

        Inquiry inquiry = inquiryReply.getInquiry();
        inquiry.updateStatus(InquiryStatus.PENDING);

        return InquiryReplyDeleteResponse.from(inquiryReply);
    }
}
