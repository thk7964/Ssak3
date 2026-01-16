package com.example.ssak3.domain.timedeal.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.common.model.PageResponse;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import com.example.ssak3.domain.timedeal.model.response.TimeDealListGetResponse;
import com.example.ssak3.domain.timedeal.model.response.TimeDealGetResponse;
import com.example.ssak3.domain.timedeal.repository.TimeDealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TimeDealService {

    private final TimeDealRepository timeDealRepository;

    /**
     * 타임딜 상세 조회
     */
    @Transactional(readOnly = true)
    public TimeDealGetResponse getTimeDeal(Long timeDealId) {

        TimeDeal response = timeDealRepository.findById(timeDealId)
                .orElseThrow(() -> new CustomException(ErrorCode.TIME_DEAL_NOT_FOUND)
                );

        return TimeDealGetResponse.from(response);
    }

    /**
     * 타임딜 목록(전체) 조회
     */
    @Transactional(readOnly = true)
    public PageResponse<TimeDealListGetResponse> getTimeDealList(Pageable pageable) {

        Page<TimeDealListGetResponse> responsePage = timeDealRepository.findAllByIsDeletedFalse(pageable).map(TimeDealListGetResponse::from);

        return PageResponse.from(responsePage);
    }

    /**
     * 타임딜 OPEN목록
     */
    @Transactional(readOnly = true)
    public PageResponse<TimeDealListGetResponse> getTimeDealOpenList(Pageable pageable) {

        Page<TimeDealListGetResponse> responsePage = timeDealRepository.findOpenTimeDeals(pageable);

        return PageResponse.from(responsePage);
    }

}