package com.example.ssak3.domain.inquiry.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.InquiryStatus;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.inquiry.entity.Inquiry;
import com.example.ssak3.domain.inquiry.model.request.InquiryCreateRequest;
import com.example.ssak3.domain.inquiry.model.response.InquiryCreateResponse;
import com.example.ssak3.domain.inquiry.model.response.InquiryGetResponse;
import com.example.ssak3.domain.inquiry.repository.InquiryRepository;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final UserRepository userRepository;


    /**
     * 문의 생성 API
     **/
    @Transactional
    public InquiryCreateResponse createInquiry(Long userId, InquiryCreateRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Inquiry inquiry = new Inquiry(
                user,
                request.getTitle(),
                request.getContent(),
                InquiryStatus.PENDING.name()
        );

        Inquiry savedInquiry = inquiryRepository.save(inquiry);

        return InquiryCreateResponse.from(savedInquiry);
    }


    /**
     * 문의 상세 조회 API
     **/
    @Transactional(readOnly = true)
    public InquiryGetResponse getInquiry(Long userId, Long inquiryId) {

        Inquiry foundInquiry =inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new CustomException(ErrorCode.INQUIRY_NOT_FOUND));

        foundInquiry.validateUser(userId);  // 작성자 검증

        return InquiryGetResponse.from(foundInquiry);
    }

}
