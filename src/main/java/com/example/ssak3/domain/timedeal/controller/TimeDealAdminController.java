package com.example.ssak3.domain.timedeal.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.domain.timedeal.model.request.TimeDealCreateRequest;
import com.example.ssak3.domain.timedeal.model.request.TimeDealUpdateRequest;
import com.example.ssak3.domain.timedeal.service.TimeDealAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class TimeDealAdminController {

    private final TimeDealAdminService timeDealAdminService;

    @PostMapping("/time-deals")
    public ResponseEntity<ApiResponse> createTimeDealApi(@Valid @RequestBody TimeDealCreateRequest request){

        ApiResponse response = ApiResponse.success("타임딜 상품 생성", timeDealAdminService.createTimeDeal(request));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/time-deals/{timeDealsId}")
    public ResponseEntity<ApiResponse> updateTimeDealApi(@PathVariable Long timeDealsId, @Valid @RequestBody TimeDealUpdateRequest request){

        ApiResponse response = ApiResponse.success("타임딜 상품 수정", timeDealAdminService.updateTimeDeal(timeDealsId ,request));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/time-deals/{timeDealsId}")
    public ResponseEntity<ApiResponse> deleteTimeDealApi(@PathVariable Long timeDealsId){

        timeDealAdminService.deleteTimeDeal(timeDealsId);

        ApiResponse response = ApiResponse.success("타임딜 상품 삭제", null);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
