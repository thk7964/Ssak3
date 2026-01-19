package com.example.ssak3.domain.timedeal.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.product.repository.ProductRepository;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import com.example.ssak3.domain.timedeal.model.request.TimeDealCreateRequest;
import com.example.ssak3.domain.timedeal.model.request.TimeDealUpdateRequest;
import com.example.ssak3.domain.timedeal.model.response.TimeDealCreateResponse;
import com.example.ssak3.domain.timedeal.model.response.TimeDealDeleteResponse;
import com.example.ssak3.domain.timedeal.model.response.TimeDealUpdateResponse;
import com.example.ssak3.domain.timedeal.repository.TimeDealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TimeDealAdminService {

    private final TimeDealRepository timeDealRepository;
    private final ProductRepository productRepository;

    /**
     * 타임딜 생성
     */
    @Transactional
    public TimeDealCreateResponse createTimeDeal(TimeDealCreateRequest request) {

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        if (product.getPrice() <= request.getDealPrice()) {
            throw new CustomException(ErrorCode.SALE_PRICE_MUST_BE_LOWER_THAN_ORIGINAL_PRICE);
        }

        if (request.getStartAt().isAfter(request.getEndAt())) {
            throw new CustomException(ErrorCode.INVALID_TIME_RANGE);
        }

        TimeDeal timeDeal = new TimeDeal(
                product,
                request.getDealPrice(),
                request.getStartAt(),
                request.getEndAt()
        );

        TimeDeal saved = timeDealRepository.save(timeDeal);

        return TimeDealCreateResponse.from(saved);

    }

    /**
     * 타임딜 수정
     */
    @Transactional
    public TimeDealUpdateResponse updateTimeDeal(Long timeDealId, TimeDealUpdateRequest request) {
        TimeDeal timeDeal = timeDealRepository.findById(timeDealId)
                .orElseThrow(() -> new CustomException(ErrorCode.TIME_DEAL_NOT_FOUND));

        if (timeDeal.getDealPrice() <= request.getDealPrice()) {
            throw new CustomException(ErrorCode.UPDATED_SALE_PRICE_MUST_BE_LOWER_THAN_CURRENT_SALE_PRICE);
        }

        if (request.getStartAt().isAfter(request.getEndAt())) {
            throw new CustomException(ErrorCode.INVALID_TIME_RANGE);
        }

        timeDeal.update(request);

        return TimeDealUpdateResponse.from(timeDeal);
    }

    /**
     * 타임딜 삭제
     */
    @Transactional
    public TimeDealDeleteResponse deleteTimeDeal(Long timeDealId) {
        TimeDeal timeDeal = timeDealRepository.findById(timeDealId)
                .orElseThrow(() -> new CustomException(ErrorCode.TIME_DEAL_NOT_FOUND));

        timeDeal.softDelete();

        return TimeDealDeleteResponse.from(timeDeal);
    }

}
