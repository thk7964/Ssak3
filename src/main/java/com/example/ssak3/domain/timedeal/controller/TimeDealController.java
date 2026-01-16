package com.example.ssak3.domain.timedeal.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.domain.timedeal.service.TimeDealService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ssak3/time-deals")
public class TimeDealController {

    private final TimeDealService timeDealService;

    /**
     * 타임딜 상세 조회
     */
    @GetMapping("/{timeDealId}")
    public ResponseEntity<ApiResponse> getTimeDealApi(@PathVariable Long timeDealId) {

        ApiResponse response = ApiResponse.success("타임딜 상세 조회", timeDealService.getTimeDeal(timeDealId));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 타임딜 목록 조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getTimeDealListApi(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        ApiResponse response = ApiResponse.success("타임딜 목록 조회", timeDealService.getTimeDealList(pageable));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * open된 타임딜 목록 조회
     */
    @GetMapping("/open")
    public ResponseEntity<ApiResponse> getTimeDealOpenListApi(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        ApiResponse response = ApiResponse.success("타임딜 OPEN 목록 조회", timeDealService.getTimeDealOpenList(pageable));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
