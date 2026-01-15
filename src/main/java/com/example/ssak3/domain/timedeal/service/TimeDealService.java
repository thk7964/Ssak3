package com.example.ssak3.domain.timedeal.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.common.model.PageResponse;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import com.example.ssak3.domain.timedeal.model.response.TimeDealGetListResponse;
import com.example.ssak3.domain.timedeal.model.response.TimeDealGetResponse;
import com.example.ssak3.domain.timedeal.repository.TimeDealRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.undo.CannotUndoException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeDealService {

    private final TimeDealRepository timeDealRepository;

    @Transactional(readOnly = true)
    public TimeDealGetResponse getTimeDeal(Long timeDealsId) {

        TimeDeal response =timeDealRepository.findById(timeDealsId)
                .orElseThrow(() -> new CustomException(ErrorCode.TIME_DEAL_NOT_FOUND)
                );


        return TimeDealGetResponse.from(response);
    }

    @Transactional(readOnly = true)
    public PageResponse<TimeDealGetListResponse> getTimeDealList(Pageable pageable) {

        Page<TimeDealGetListResponse> responsePage =timeDealRepository.findAllByIsDeletedFalse(pageable).map(TimeDealGetListResponse::from);

        return PageResponse.from(responsePage);
    }

}