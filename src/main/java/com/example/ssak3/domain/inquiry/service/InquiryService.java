package com.example.ssak3.domain.inquiry.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.InquiryStatus;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.inquiry.entity.Inquiry;
import com.example.ssak3.domain.inquiry.model.request.InquiryCreateRequest;
import com.example.ssak3.domain.inquiry.model.request.InquiryUpdateRequest;
import com.example.ssak3.domain.inquiry.model.response.InquiryCreateResponse;
import com.example.ssak3.domain.inquiry.model.response.InquiryDeleteResponse;
import com.example.ssak3.domain.inquiry.model.response.InquiryGetResponse;
import com.example.ssak3.domain.inquiry.model.response.InquiryUpdateResponse;
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
     * 문의 생성
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
     * 문의 상세 조회
     **/
    @Transactional(readOnly = true)
    public InquiryGetResponse getInquiry(Long userId, Long inquiryId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Inquiry foundInquiry =inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new CustomException(ErrorCode.INQUIRY_NOT_FOUND));

        foundInquiry.validateUser(user.getId());  // 작성자 검증

        return InquiryGetResponse.from(foundInquiry);
    }

    /**
     * 문의 수정
     **/
    @Transactional
    public InquiryUpdateResponse updateInquiry(Long userId, Long inquiryId, InquiryUpdateRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Inquiry foundInquiry =inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new CustomException(ErrorCode.INQUIRY_NOT_FOUND));

        foundInquiry.validateUser(user.getId());  // 작성자 검증
        foundInquiry.validateAnswered(); // 이미 답변완료된 문의인지 검증(답변완료된 것은 수정불가)

        foundInquiry.update(request.getTitle(), request.getContent());

        return InquiryUpdateResponse.from(foundInquiry);
    }

    /**
     * 문의 삭제
     **/
    @Transactional
    public InquiryDeleteResponse deleteInquiry(Long userId, Long inquiryId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Inquiry foundInquiry =inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new CustomException(ErrorCode.INQUIRY_NOT_FOUND));

        foundInquiry.validateUser(user.getId());  // 작성자 검증
        foundInquiry.validateAnswered();  // 이미 답변완료된 문의인지 검증(답변완료된 것은 삭제불가)

        foundInquiry.delete();

        return InquiryDeleteResponse.from(foundInquiry);
    }

}
