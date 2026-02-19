package com.example.ssak3.domain.timedeal.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.TimeDealStatus;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.common.model.PageResponse;
import com.example.ssak3.domain.product.service.ProductRankingService;
import com.example.ssak3.domain.s3.service.S3Uploader;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import com.example.ssak3.domain.timedeal.model.response.TimeDealGetResponse;
import com.example.ssak3.domain.timedeal.model.response.TimeDealListGetResponse;
import com.example.ssak3.domain.timedeal.repository.TimeDealRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TimeDealService {

    private final TimeDealRepository timeDealRepository;
    private final ProductRankingService productRankingService;
    private final S3Uploader s3Uploader;

    /**
     * 타임딜 상세 조회
     */
    @Transactional(readOnly = true)
    public TimeDealGetResponse getTimeDeal(Long timeDealId, String ip) {

        TimeDeal response = timeDealRepository.findByIdAndIsDeletedFalse(timeDealId)
                .orElseThrow(() -> new CustomException(ErrorCode.TIME_DEAL_NOT_FOUND));

        try {
            productRankingService.increaseViewCount(response.getProduct().getId(), ip);
        } catch (Exception e) {
            log.warn("Redis 조회수 업데이트 실패: productId = {}", response.getProduct().getId());
        }

        String imageUrl = s3Uploader.createPresignedGetUrl(response.getImage(), 5);
        String detailImageUrl = s3Uploader.createPresignedGetUrl(response.getDetailImage(), 5);
        String productImageUrl = s3Uploader.createPresignedGetUrl(response.getProduct().getImage(), 5);
        String productDetailImageUrl = s3Uploader.createPresignedGetUrl(response.getProduct().getDetailImage(), 5);

        return TimeDealGetResponse.from(response, imageUrl, detailImageUrl, productImageUrl, productDetailImageUrl);
    }

    /**
     * 타임딜 상태별 목록 조회
     */
    @Cacheable(
            cacheManager = "redisCacheManager",
            value = "timeDealsOpen",
            key = "#pageable.pageNumber + ':' + #pageable.pageSize",
            condition = "#status != null && #status.equalsIgnoreCase('OPEN') && #pageable.pageNumber <= 1"
    )
    @Transactional(readOnly = true)
    public PageResponse<TimeDealListGetResponse> getTimeDealStatusList(String status, Pageable pageable) {

        TimeDealStatus timeDealStatus = parseStatus(status);

        Page<TimeDealListGetResponse> responsePage = timeDealRepository.findTimeDeals(timeDealStatus, pageable);

        return PageResponse.from(responsePage);
    }

    private TimeDealStatus parseStatus(String status) {

        if (status == null) {
            return null;
        }

        try {
            TimeDealStatus parsed = TimeDealStatus.valueOf(status.toUpperCase());

            if (parsed == TimeDealStatus.DELETED) {
                throw new CustomException(ErrorCode.TIME_DEAL_DELETED_STATUS_NOT_ALLOWED);
            }

            return parsed;
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.TIME_DEAL_INVALID_STATUS);
        }
    }
}