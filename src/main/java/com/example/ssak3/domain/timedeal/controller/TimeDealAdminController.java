package com.example.ssak3.domain.timedeal.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.domain.timedeal.model.request.TimeDealCreateRequest;
import com.example.ssak3.domain.timedeal.model.request.TimeDealUpdateRequest;
import com.example.ssak3.domain.timedeal.service.TimeDealAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ssak3/admin/time-deals")
public class TimeDealAdminController {

    private final TimeDealAdminService timeDealAdminService;

    /**
     * 타임딜 생성 API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse> createTimeDealApi(@Valid @RequestBody TimeDealCreateRequest request) {

        ApiResponse response = ApiResponse.success("타임딜 상품 생성에 성공했습니다.", timeDealAdminService.createTimeDeal(request));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 타임딜 목록 조회 API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse> getTimeDealListApi(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        ApiResponse response = ApiResponse.success("타임딜 목록 조회에 성공했습니다.", timeDealAdminService.getTimeDealList(pageable));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 타임딜 수정 API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{timeDealId}")
    public ResponseEntity<ApiResponse> updateTimeDealApi(@PathVariable Long timeDealId, @RequestBody TimeDealUpdateRequest request) {

        ApiResponse response = ApiResponse.success("타임딜 상품 수정에 성공했습니다.", timeDealAdminService.updateTimeDeal(timeDealId, request));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 타임딜 삭제 API
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{timeDealId}")
    public ResponseEntity<ApiResponse> deleteTimeDealApi(@PathVariable Long timeDealId) {

        ApiResponse response = ApiResponse.success("타임딜 상품 삭제에 성공했습니다.", timeDealAdminService.deleteTimeDeal(timeDealId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
