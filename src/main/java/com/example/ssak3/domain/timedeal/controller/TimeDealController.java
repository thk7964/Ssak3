package com.example.ssak3.domain.timedeal.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.domain.timedeal.service.TimeDealService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ssak3/time-deals")
public class TimeDealController {

    private final TimeDealService timeDealService;

    /**
     * 타임딜 상세 조회 API
     */
    @GetMapping("/{timeDealId}")
    public ResponseEntity<ApiResponse> getTimeDealApi(@PathVariable Long timeDealId) {

        ApiResponse response = ApiResponse.success("타임딜 상세 조회에 성공했습니다.", timeDealService.getTimeDeal(timeDealId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 타임딜 상태별 목록 조회 API
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getTimeDealStatusListApi(@RequestParam(required = false) String status, @PageableDefault Pageable pageable) {

        ApiResponse response = ApiResponse.success("타임딜 상태별 목록 조회에 성공했습니다.", timeDealService.getTimeDealStatusList(status, pageable));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
