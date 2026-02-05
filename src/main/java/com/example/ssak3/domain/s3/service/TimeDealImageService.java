package com.example.ssak3.domain.s3.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.s3.model.response.TimeDealImageGetResponse;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import com.example.ssak3.domain.timedeal.repository.TimeDealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class TimeDealImageService {

    private final S3Uploader s3Uploader;
    private final TimeDealRepository timeDealRepository;

    /**
     * 타임딜 이미지 업로드
     */
    @Transactional
    public TimeDealImageGetResponse uploadTimeDealImage(Long timeDealId, MultipartFile multipartFile) {
        TimeDeal timeDeal = timeDealRepository.findByIdAndIsDeletedFalse(timeDealId)
                .orElseThrow(() -> new CustomException(ErrorCode.TIME_DEAL_NOT_FOUND));

        if (timeDeal.getImage() != null) {
            throw new CustomException(ErrorCode.TIME_DEAL_IMAGE_ALREADY_EXIST);
        }

        s3Uploader.uploadTimeDealImage(timeDeal, multipartFile, "timeDeals");

        return TimeDealImageGetResponse.from(timeDeal);
    }

    /**
     * 타임딜 이미지 가져오기
     */
    @Transactional(readOnly = true)
    public TimeDealImageGetResponse getTimeDealImage(Long timeDealId) {

        TimeDeal timeDeal = timeDealRepository.findByIdAndIsDeletedFalse(timeDealId)
                .orElseThrow(() -> new CustomException(ErrorCode.TIME_DEAL_NOT_FOUND));

        return TimeDealImageGetResponse.from(timeDeal);
    }

    /**
     * 타임딜 이미지 수정
     */
    @Transactional
    public TimeDealImageGetResponse updateTimeDealImage(Long timeDealId, MultipartFile multipartFile) {
        TimeDeal timeDeal = timeDealRepository.findByIdAndIsDeletedFalse(timeDealId)
                .orElseThrow(() -> new CustomException(ErrorCode.TIME_DEAL_NOT_FOUND));

        String oldUrl = timeDeal.getImage();

        s3Uploader.uploadTimeDealImage(timeDeal, multipartFile, "timeDeals");
        s3Uploader.deleteImage(oldUrl);

        return TimeDealImageGetResponse.from(timeDeal);
    }

    /**
     * 타임딜 이미지 삭제
     */
    @Transactional
    public TimeDealImageGetResponse deleteTimeDealImage(Long timeDealId) {
        TimeDeal timeDeal = timeDealRepository.findByIdAndIsDeletedFalse(timeDealId)
                .orElseThrow(() -> new CustomException(ErrorCode.TIME_DEAL_NOT_FOUND));

        s3Uploader.deleteImage(timeDeal.getImage());
        timeDeal.setImage(null);

        return  TimeDealImageGetResponse.from(timeDeal);
    }
}
