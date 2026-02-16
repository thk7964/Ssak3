package com.example.ssak3.domain.review.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.OrderStatus;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.common.model.PageResponse;
import com.example.ssak3.domain.orderProduct.repository.OrderProductRepository;
import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.product.repository.ProductRepository;
import com.example.ssak3.domain.review.entity.Review;
import com.example.ssak3.domain.review.model.request.ReviewCreateRequest;
import com.example.ssak3.domain.review.model.request.ReviewUpdateRequest;
import com.example.ssak3.domain.review.model.response.*;
import com.example.ssak3.domain.review.repository.ReviewRepository;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;

    /**
     * 후기 생성
     */
    @Transactional
    public ReviewCreateResponse createReview(Long userId, ReviewCreateRequest request) {

        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!orderProductRepository.existsByOrderUserIdAndProductIdAndOrderStatus(user.getId(), request.getProductId(), OrderStatus.DONE)) {
            throw new CustomException(ErrorCode.USER_NOT_PURCHASED_PRODUCT);
        }

        if (reviewRepository.existsByUserIdAndProductIdAndIsDeletedFalse(user.getId(), request.getProductId())) {
            throw new CustomException(ErrorCode.ALREADY_REVIEWED);
        }

        Product product = productRepository.findByIdAndIsDeletedFalse(request.getProductId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        Review createReview = new Review(
                user,
                product,
                request.getContent(),
                request.getScore()
        );

        Review savedReview = reviewRepository.save(createReview);

        Double averageScoreByProductId = reviewRepository.findAverageScoreByProductId(product.getId());

        Double roundedAvgScore = BigDecimal.valueOf(averageScoreByProductId)
                .setScale(1, RoundingMode.HALF_UP)
                .doubleValue();

        product.updateAverageScore(roundedAvgScore);

        return ReviewCreateResponse.from(savedReview);
    }

    /**
     * 후기 상세 조회 (삭제 가능?)
     */
    @Transactional(readOnly = true)
    public ReviewGetResponse getReview(Long reviewId) {

        Review review = findReview(reviewId);

        return ReviewGetResponse.from(review);
    }

    /**
     * 후기 목록 조회
     */
    @Transactional(readOnly = true)
    public ReviewPageResponse<ReviewListGetResponse> getReviewList(Long productId, Pageable pageable) {

        Page<ReviewListGetResponse> reviewPage = reviewRepository.findByProductIdAndIsDeletedFalse(productId, pageable)
                .map(ReviewListGetResponse::from);

        Double averageScore = reviewRepository.findAverageScoreByProductId(productId);

        Double roundedAvgScore = BigDecimal.valueOf(averageScore)
                .setScale(1, RoundingMode.HALF_UP)
                .doubleValue();

        return ReviewPageResponse.from(reviewPage, roundedAvgScore);
    }

    /**
     * 내가 쓴 후기 목록 조회
     */
    @Transactional(readOnly = true)
    public PageResponse<ReviewListGetResponse> getMyReviewList(Long userId, Pageable pageable) {

        Page<ReviewListGetResponse> reviewPage = reviewRepository.findByUserIdAndIsDeletedFalse(userId, pageable)
                .map(ReviewListGetResponse::from);

        return PageResponse.from(reviewPage);
    }

    /**
     * 후기 수정
     */
    @Transactional
    public ReviewUpdateResponse updateReview(Long userId, Long reviewId, ReviewUpdateRequest request) {

        Review review = findReview(reviewId);

        if (!userId.equals(review.getUser().getId())) {
            throw new CustomException(ErrorCode.REVIEW_AUTHOR_MISMATCH);
        }

        review.update(request);

        return ReviewUpdateResponse.from(review);
    }

    /**
     * 후기 삭제
     */
    @Transactional
    public ReviewDeleteResponse deleteReview(Long userId, Long reviewId) {

        Review review = findReview(reviewId);

        if (!userId.equals(review.getUser().getId())) {
            throw new CustomException(ErrorCode.REVIEW_AUTHOR_MISMATCH);
        }

        review.softDelete();

        return ReviewDeleteResponse.from(review);
    }

    private Review findReview(Long reviewId) {

        return reviewRepository.findByIdAndIsDeletedFalse(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
    }
}
