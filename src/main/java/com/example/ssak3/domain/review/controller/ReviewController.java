package com.example.ssak3.domain.review.controller;

import com.example.ssak3.common.model.ApiResponse;
import com.example.ssak3.common.model.AuthUser;
import com.example.ssak3.domain.review.model.request.ReviewCreateRequest;
import com.example.ssak3.domain.review.model.request.ReviewUpdateRequest;
import com.example.ssak3.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ssak3/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 후기 생성 API
     */
    @PostMapping
    public ResponseEntity<ApiResponse> createReviewApi(@AuthenticationPrincipal AuthUser user, @Valid @RequestBody ReviewCreateRequest request) {

        ApiResponse response = ApiResponse.success("후기 생성에 성공하였습니다.", reviewService.createReview(user.getId(), request));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 후기 상세 조회 API
     */
    @GetMapping("/{reviewId}")
    public ResponseEntity<ApiResponse> getReviewApi(@PathVariable Long reviewId) {

        ApiResponse response = ApiResponse.success("후기 상세 조회에 성공했습니다.", reviewService.getReview(reviewId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 후기 목록 조회 API
     */
    @GetMapping
    public ResponseEntity<ApiResponse> getReviewListApi(@RequestParam Long productId, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        ApiResponse response = ApiResponse.success("후기 목록 조회에 성공했습니다.", reviewService.getReviewList(productId, pageable));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 내가 쓴 후기 목록 조회 API
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getMyReviewListApi(@AuthenticationPrincipal AuthUser user, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        ApiResponse response = ApiResponse.success("내가 쓴 후기목록을 조회하였습니다.", reviewService.getMyReviewList(user.getId(), pageable));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 후기 수정 API
     */
    @PatchMapping("/{reviewId}")
    public ResponseEntity<ApiResponse> updateReviewApi(@AuthenticationPrincipal AuthUser user, @PathVariable Long reviewId, @Valid @RequestBody ReviewUpdateRequest request) {

        ApiResponse response = ApiResponse.success("후기를 수정하였습니다.", reviewService.updateReview(user.getId(), reviewId, request));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * 후기 삭제 API
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReviewApi(@AuthenticationPrincipal AuthUser user, @PathVariable Long reviewId) {

        ApiResponse response = ApiResponse.success("후기를 삭제했습니다.", reviewService.deleteReview(user.getId(), reviewId));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
