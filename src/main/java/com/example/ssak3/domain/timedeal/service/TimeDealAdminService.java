package com.example.ssak3.domain.timedeal.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.ProductStatus;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.product.repository.ProductRepository;
import com.example.ssak3.domain.s3.service.S3Uploader;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import com.example.ssak3.domain.timedeal.model.request.TimeDealCreateRequest;
import com.example.ssak3.domain.timedeal.model.request.TimeDealUpdateRequest;
import com.example.ssak3.domain.timedeal.model.response.TimeDealCreateResponse;
import com.example.ssak3.domain.timedeal.model.response.TimeDealDeleteResponse;
import com.example.ssak3.domain.timedeal.model.response.TimeDealUpdateResponse;
import com.example.ssak3.domain.timedeal.repository.TimeDealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TimeDealAdminService {

    private final TimeDealRepository timeDealRepository;
    private final ProductRepository productRepository;
    private final S3Uploader s3Uploader;

    /**
     * 타임딜 생성
     */
    @Transactional
    public TimeDealCreateResponse createTimeDeal(TimeDealCreateRequest request) {

        Product product = productRepository.findByIdAndIsDeletedFalse(request.getProductId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        if (product.getStatus() == ProductStatus.STOP_SALE || product.getStatus() == ProductStatus.SOLD_OUT) {
            throw new CustomException(ErrorCode.TIME_DEAL_CANNOT_CREATE);
        }

        boolean hasActiveDeal = timeDealRepository.existsActiveDealByProduct(product.getId());

        if (hasActiveDeal) {
            throw new CustomException(ErrorCode.ACTIVE_TIME_DEAL_ALREADY_EXISTS);
        }

        if (product.getPrice() <= request.getDealPrice()) {
            throw new CustomException(ErrorCode.SALE_PRICE_MUST_BE_LOWER_THAN_ORIGINAL_PRICE);
        }

        LocalDateTime now = LocalDateTime.now();

        if (!request.getStartAt().isAfter(now)) {
            throw new CustomException(ErrorCode.TIME_DEAL_START_TIME_MUST_BE_IN_FUTURE);
        }

        if (!request.getEndAt().isAfter(request.getStartAt())) {
            throw new CustomException(ErrorCode.INVALID_TIME_RANGE);
        }

        TimeDeal timeDeal = new TimeDeal(
                product,
                request.getDealPrice(),
                request.getStartAt(),
                request.getEndAt(),
                request.getImage(),
                request.getDetailImage()
        );

        TimeDeal saved = timeDealRepository.save(timeDeal);

        return TimeDealCreateResponse.from(saved);
    }

    /**
     * 타임딜 수정
     */
    @CacheEvict(value = "timeDealsOpen", allEntries = true)
    @Transactional
    public TimeDealUpdateResponse updateTimeDeal(Long timeDealId, TimeDealUpdateRequest request) {

        TimeDeal timeDeal = timeDealRepository.findByIdAndIsDeletedFalse(timeDealId)
                .orElseThrow(() -> new CustomException(ErrorCode.TIME_DEAL_NOT_FOUND));

        if (timeDeal.getProduct().getStatus() == ProductStatus.SOLD_OUT) {
            throw new CustomException(ErrorCode.TIME_DEAL_CANNOT_UPDATE);
        }

        if (request.getDealPrice() != null && request.getDealPrice() >= timeDeal.getProduct().getPrice()) {
            throw new CustomException(ErrorCode.SALE_PRICE_MUST_BE_LOWER_THAN_ORIGINAL_PRICE);
        }

        LocalDateTime now = LocalDateTime.now();

        if (request.getStartAt() != null) {
            if (!request.getStartAt().isAfter(now)) {
                throw new CustomException(ErrorCode.TIME_DEAL_START_TIME_MUST_BE_IN_FUTURE);
            }
        }

        LocalDateTime startAt = request.getStartAt() != null ? request.getStartAt() : timeDeal.getStartAt();
        LocalDateTime endAt = request.getEndAt() != null ? request.getEndAt() : timeDeal.getEndAt();

        if (request.getStartAt() != null || request.getEndAt() != null) {
            if (!endAt.isAfter(startAt)) {
                throw new CustomException(ErrorCode.INVALID_TIME_RANGE);
            }
        }

        if (request.getImage() != null && timeDeal.getImage() != null) {
            s3Uploader.deleteImage(timeDeal.getImage());
        }

        if (request.getDetailImage() != null && timeDeal.getDetailImage() != null) {
            s3Uploader.deleteImage(timeDeal.getDetailImage());
        }

        timeDeal.update(request);

        return TimeDealUpdateResponse.from(timeDeal);
    }

    /**
     * 타임딜 삭제
     */
    @Transactional
    public TimeDealDeleteResponse deleteTimeDeal(Long timeDealId) {

        TimeDeal timeDeal = timeDealRepository.findByIdAndIsDeletedFalse(timeDealId)
                .orElseThrow(() -> new CustomException(ErrorCode.TIME_DEAL_NOT_FOUND));

        if (!timeDeal.isDeletable()) {
            throw new CustomException(ErrorCode.TIME_DEAL_CANNOT_DELETE);
        }

        if (timeDeal.getImage() != null) {
            s3Uploader.deleteImage(timeDeal.getImage());
        }

        if (timeDeal.getDetailImage() != null) {
            s3Uploader.deleteImage(timeDeal.getDetailImage());
        }

        timeDeal.softDelete();

        return TimeDealDeleteResponse.from(timeDeal);
    }
}
