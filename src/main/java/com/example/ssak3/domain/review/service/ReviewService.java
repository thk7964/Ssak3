package com.example.ssak3.domain.review.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.common.model.AuthUser;
import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.product.repository.ProductRepository;
import com.example.ssak3.domain.review.entity.Review;
import com.example.ssak3.domain.review.model.request.ReviewCreateRequest;
import com.example.ssak3.domain.review.model.request.ReviewUpdateRequest;
import com.example.ssak3.domain.review.model.response.ReviewCreateResponse;
import com.example.ssak3.domain.review.model.response.ReviewDeleteResponse;
import com.example.ssak3.domain.review.model.response.ReviewUpdateResponse;
import com.example.ssak3.domain.review.repository.ReviewRepository;
import com.example.ssak3.domain.user.entity.User;
import com.example.ssak3.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    /**
     * 후기 생성
     */
    @Transactional
    public ReviewCreateResponse createReview(ReviewCreateRequest request) {
        User foundUser = userRepository.findByIdAndIsDeletedFalse(request.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Product foundProduct = productRepository.findByIdAndIsDeletedFalse(request.getProductId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        Review createReview = new Review(
                foundUser,
                foundProduct,
                request.getContent(),
                request.getScore()
        );
        Review savedReview = reviewRepository.save(createReview);
        return ReviewCreateResponse.from(savedReview);
    }

    /**
     * 후기 조회
     */
    @Transactional(readOnly = true)
    public ReviewCreateResponse getReview(Long reviewId) {
        Review foundReview = reviewRepository.findByIdAndIsDeletedFalse(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

       return ReviewCreateResponse.from(foundReview);
    }

    /**
     * 후기 수정
     */
    @Transactional
    public ReviewUpdateResponse updateReview(AuthUser user, Long reviewId, ReviewUpdateRequest request) {

        Review foundReview = reviewRepository.findByIdAndIsDeletedFalse(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        // 사용자와 댓글 작성자가 일치하는지 확인
        if(!user.getId().equals(foundReview.getId())) {
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

        log.info("service: {}", reviewId);

        Review foundReview = reviewRepository.findByIdAndIsDeletedFalse(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));

        // 사용자와 댓글 작성자가 일치하는지 확인
        if (!user.getId().equals(foundReview.getId())) {
            throw new CustomException(ErrorCode.REVIEW_AUTHOR_MISMATCH);
        }
        foundReview.softDelete();
        return ReviewDeleteResponse.from(foundReview);
    }
}
