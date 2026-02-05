package com.example.ssak3.domain.productviewhistory.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.product.repository.ProductRepository;
import com.example.ssak3.domain.productviewhistory.entity.ProductViewHistory;
import com.example.ssak3.domain.productviewhistory.repository.ProductViewHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductViewBackUpService {

    private static final String PRODUCT_DAILY_RANKING_PREFIX = "product:ranking:";
    private final ProductViewHistoryRepository productViewHistoryRepository;
    private final ProductRepository productRepository;
    private final StringRedisTemplate redisTemplate;

    @Transactional
    public void backUpViewCount() {

        LocalDate now = LocalDate.now();
        String key = PRODUCT_DAILY_RANKING_PREFIX + now;

        Set<ZSetOperations.TypedTuple<String>> redisData = null;

        try {
            // Redis에서 값 가져오기
             redisData = redisTemplate.opsForZSet().rangeWithScores(key, 0, -1);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.REDIS_CONNECTION_ERROR);
        }

        if (redisData == null || redisData.isEmpty()) {
            return;
        }

        // 변환
        List<ProductViewHistory> histories = redisData.stream()
                .map(tuple -> {
                    Long productId = Long.parseLong(tuple.getValue());
                    int viewCount = tuple.getScore().intValue();

                    Product product = productRepository.getReferenceById(productId);

                    return new ProductViewHistory(product, now, viewCount);
                })
                .toList();

        // DB에 저장하기
        productViewHistoryRepository.saveAll(histories);
    }
}
