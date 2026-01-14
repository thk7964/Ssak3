package com.example.ssak3.domain.timedeal.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.product.repository.ProductRepository;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import com.example.ssak3.domain.timedeal.model.request.TimeDealCreateRequest;
import com.example.ssak3.domain.timedeal.model.request.TimeDealUpdateRequest;
import com.example.ssak3.domain.timedeal.model.response.TimeDealCreateResponse;
import com.example.ssak3.domain.timedeal.model.response.TimeDealUpdateResponse;
import com.example.ssak3.domain.timedeal.repository.TimeDealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TimeDealAdminService {

    private final TimeDealRepository timeDealRepository;
    private final ProductRepository productRepository;

    public TimeDealCreateResponse createTimeDeal(TimeDealCreateRequest request) {

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        if (product.getPrice()<= request.getDealPrice()){
            throw new CustomException(ErrorCode.INVALID_SALE_PRICE_BELOW_ORIGINAL);
        }

        if (request.getStartAt().isAfter(request.getEndAt())) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
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

    public TimeDealUpdateResponse updateTimeDeal(Long timeDealsId, TimeDealUpdateRequest request) {
        TimeDeal timeDeal = timeDealRepository.findById(timeDealsId)
                .orElseThrow(()-> new CustomException(ErrorCode.TIME_DEAL_NOT_FOUND));

        if (timeDeal.getDealPrice()<= request.getDealPrice()){
            throw new CustomException(ErrorCode.INVALID_SALE_PRICE_BELOW_PREVIOUS);
        }

        timeDeal.update(request);

        return TimeDealUpdateResponse.from(timeDeal);
    }

}
