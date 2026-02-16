package com.example.ssak3.domain.inquiry.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.InquiryStatus;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.common.model.PageResponse;
import com.example.ssak3.domain.inquiry.entity.Inquiry;
import com.example.ssak3.domain.inquiry.model.request.InquiryCreateRequest;
import com.example.ssak3.domain.inquiry.model.request.InquiryUpdateRequest;
import com.example.ssak3.domain.inquiry.model.response.*;
import com.example.ssak3.domain.inquiry.repository.InquiryRepository;
import com.example.ssak3.domain.inquiryreply.entity.InquiryReply;
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
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final InquiryReplyRepository inquiryReplyRepository;
    private final UserRepository userRepository;

    /**
     * 문의 생성
     */
    @Transactional
    public InquiryCreateResponse createInquiry(Long userId, InquiryCreateRequest request) {

        User user = findUser(userId);

        Inquiry inquiry = new Inquiry(
                user,
                request.getTitle(),
                request.getContent(),
                InquiryStatus.PENDING
        );

        Inquiry savedInquiry = inquiryRepository.save(inquiry);

        return InquiryCreateResponse.from(savedInquiry);
    }

    /**
     * 문의 전체 조회
     */
    @Transactional(readOnly = true)
    public PageResponse<InquiryListGetResponse> getInquiryList(Long userId, Pageable pageable) {

        User user = findUser(userId);

        Page<InquiryListGetResponse> inquiryListPage = inquiryRepository.findAllByIsDeletedFalseAndUserId(user.getId(), pageable).map(InquiryListGetResponse::from);

        return PageResponse.from(inquiryListPage);
    }

    /**
     * 문의 상세 조회
     */
    @Transactional(readOnly = true)
    public InquiryGetResponse getInquiry(Long userId, Long inquiryId) {

        User user = findUser(userId);

        Inquiry inquiry = findInquiry(inquiryId);

        InquiryReply inquiryReply = inquiryReplyRepository.findByInquiryIdAndIsDeletedFalse(inquiryId)
                .orElse(null);

        inquiry.validateUser(user.getId());

        return InquiryGetResponse.from(inquiry, inquiryReply);
    }

    /**
     * 문의 수정
     */
    @Transactional
    public InquiryUpdateResponse updateInquiry(Long userId, Long inquiryId, InquiryUpdateRequest request) {

        User user = findUser(userId);

        Inquiry inquiry = findInquiry(inquiryId);

        inquiry.validateUser(user.getId());
        inquiry.validateAnswered();

        inquiry.update(request);

        return InquiryUpdateResponse.from(inquiry);
    }

    /**
     * 문의 삭제
     */
    @Transactional
    public InquiryDeleteResponse deleteInquiry(Long userId, Long inquiryId) {

        User user = findUser(userId);

        Inquiry inquiry = findInquiry(inquiryId);

        inquiry.validateUser(user.getId());
        inquiry.validateAnswered();

        inquiry.softDelete();

        return InquiryDeleteResponse.from(inquiry);
    }

    private User findUser(Long userId) {

        return userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private Inquiry findInquiry(Long inquiryId) {

        return inquiryRepository.findByIdAndIsDeletedFalse(inquiryId)
                .orElseThrow(() -> new CustomException(ErrorCode.INQUIRY_NOT_FOUND));
    }
}
