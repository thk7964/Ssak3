package com.example.ssak3.domain.timedeal.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.domain.timedeal.model.request.TimeDealCreateRequest;
import com.example.ssak3.domain.timedeal.model.request.TimeDealUpdateRequest;
import com.example.ssak3.domain.timedeal.service.TimeDealAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ssak3/admin/time-deals")
public class TimeDealAdminController {

    private final TimeDealAdminService timeDealAdminService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse> createTimeDealApi(@Valid @RequestBody TimeDealCreateRequest request){

        ApiResponse response = ApiResponse.success("타임딜 상품 생성", timeDealAdminService.createTimeDeal(request));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{timeDealsId}")
    public ResponseEntity<ApiResponse> updateTimeDealApi(@PathVariable Long timeDealsId, @Valid @RequestBody TimeDealUpdateRequest request){

        ApiResponse response = ApiResponse.success("타임딜 상품 수정", timeDealAdminService.updateTimeDeal(timeDealsId ,request));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{timeDealsId}")
    public ResponseEntity<ApiResponse> deleteTimeDealApi(@PathVariable Long timeDealsId){

        timeDealAdminService.deleteTimeDeal(timeDealsId);

        ApiResponse response = ApiResponse.success("타임딜 상품 삭제", null);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
