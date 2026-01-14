package com.example.ssak3.domain.inquiry.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.InquiryStatus;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.inquiry.entity.Inquiry;
import com.example.ssak3.domain.inquiry.model.request.InquiryCreateRequest;
import com.example.ssak3.domain.inquiry.model.response.InquiryCreateResponse;
import com.example.ssak3.domain.inquiry.repository.InquiryRepository;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

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

}
