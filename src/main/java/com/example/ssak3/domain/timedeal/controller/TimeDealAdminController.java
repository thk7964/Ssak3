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


}
