package com.example.ssak3.domain.review.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.OrderStatus;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.common.model.AuthUser;
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
import java.util.Optional;

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
    public ReviewCreateResponse createReview(AuthUser user, ReviewCreateRequest request) {

        User foundUser = userRepository.findByIdAndIsDeletedFalse(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!orderProductRepository.existsByOrderUserIdAndProductIdAndOrderStatus(foundUser.getId(), request.getProductId(), OrderStatus.DONE)) {
            throw new CustomException(ErrorCode.USER_NOT_PURCHASED_PRODUCT);
        }

        Optional<Review> review = reviewRepository.findByUserIdAndProductId(foundUser.getId(), request.getProductId());

        if (review.isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_REVIEWED);
        }

        Product foundProduct = productRepository.findByIdAndIsDeletedFalse(request.getProductId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));


        Review createReview = new Review(
                foundUser,
                foundProduct,
                request.getContent(),
                request.getScore()
        );
        Review savedReview = reviewRepository.save(createReview);

        Double averageScoreByProductId = reviewRepository.findAverageScoreByProductId(foundProduct.getId());

        Double roundedAvgScore = BigDecimal.valueOf(averageScoreByProductId)
                // 소수점 자릿수(scale)를 1자리로 설정, 반올림 규칙은 HALF_UP(5 이상 올림)
                .setScale(1, RoundingMode.HALF_UP)
                // 다시 double 타입으로 변환 why? JSON 직렬화(객체를 전송 가능한 형태로 바꾸는 과정)
                .doubleValue();

        foundProduct.updateAverageScore(roundedAvgScore);

        return ReviewCreateResponse.from(savedReview);
    }

    /**
     * 후기 상세조회
     */
    @Transactional(readOnly = true)
    public ReviewGetResponse getReview(Long reviewId) {
        Review foundReview = reviewRepository.findByIdAndIsDeletedFalse(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

       return ReviewGetResponse.from(foundReview);
    }

    /**
     * 후기 목록조회
     */
    @Transactional(readOnly = true)
    public ReviewPageResponse getReviewList(Long productId, Pageable pageable) {
        Page<ReviewListGetResponse> reviewPage = reviewRepository.findByProductIdAndIsDeletedFalse(productId, pageable)
                .map(ReviewListGetResponse::from);
        Double averageScore = reviewRepository.findAverageScoreByProductId(productId);

        // double은 2진수 기반이기 때문에 정확히 표현불가 -> BigDecimal은 10진수 기반
        Double roundedAvgScore = BigDecimal.valueOf(averageScore)
                // 소수점 자릿수(scale)를 1자리로 설정, 반올림 규칙은 HALF_UP(5 이상 올림)
                .setScale(1, RoundingMode.HALF_UP)
                // 다시 double 타입으로 변환 why? JSON 직렬화(객체를 전송 가능한 형태로 바꾸는 과정)
                .doubleValue();
        return ReviewPageResponse.from(reviewPage, roundedAvgScore);
    }

    /**
     * 내가 쓴 후기 목록조회
     */
    @Transactional(readOnly = true)
    public PageResponse getMyReviewList(AuthUser user, Pageable pageable) {

        Page<ReviewListGetResponse> reviewPage = reviewRepository.findByUserIdAndIsDeletedFalse(user.getId(), pageable)
                .map(ReviewListGetResponse::from);

        return PageResponse.from(reviewPage);
    }

    /**
     * 후기 수정
     */
    @Transactional
    public ReviewUpdateResponse updateReview(AuthUser user, Long reviewId, ReviewUpdateRequest request) {

        Review foundReview = reviewRepository.findByIdAndIsDeletedFalse(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        // 사용자와 댓글 작성자가 일치하는지 확인
        if(!user.getId().equals(foundReview.getUser().getId())) {
            throw new CustomException(ErrorCode.REVIEW_AUTHOR_MISMATCH);
        }
        foundReview.update(request);
        return ReviewUpdateResponse.from(foundReview);
    }

    /**
     * 후기 삭제
     */
    @Transactional
    public ReviewDeleteResponse deleteReview(AuthUser user, Long reviewId) {

        Review foundReview = reviewRepository.findByIdAndIsDeletedFalse(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
        log.info("service foundReview: {}", foundReview.getId());
        // 사용자와 댓글 작성자가 일치하는지 확인
        if (!user.getId().equals(foundReview.getUser().getId())) {
            throw new CustomException(ErrorCode.REVIEW_AUTHOR_MISMATCH);
        }
        log.info("service filter foundReview: {}", foundReview.getId());
        foundReview.softDelete();
        return ReviewDeleteResponse.from(foundReview);
    }
}
