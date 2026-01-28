package com.example.ssak3.domain.product.service;

import com.example.ssak3.domain.product.model.response.ProductGetPopularResponse;
import com.example.ssak3.domain.product.repository.ProductRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductCacheService {

    private final StringRedisTemplate redisTemplate;
    private static final String PRODUCT_RANKING_KEY = "product:ranking:top10";
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    /**
     * 조회 수 TOP 10 조회
     */
    @Transactional(readOnly = true)
    public List<ProductGetPopularResponse> getPopularProduct() {

        try {
            String cached = redisTemplate.opsForValue().get(PRODUCT_RANKING_KEY);

            // Cache Aside 패턴: Cache Hit
            if (cached != null) {
                return objectMapper.readValue(cached, new TypeReference<List<ProductGetPopularResponse>>() {});
            }

            // Cache Aside 패턴: Cache Miss
            List<ProductGetPopularResponse> responses = productRepository.getProductViewCountTop10();

            String json = objectMapper.writeValueAsString(responses);

            // Redis에 결과 캐싱
            redisTemplate.opsForValue().set(
                    PRODUCT_RANKING_KEY,
                    json,
                    Duration.ofHours(1)
            );

            return responses;
        } catch (Exception e) {
            log.warn("캐싱 과정에서 문제가 발생하였습니다. key={}", PRODUCT_RANKING_KEY, e);
            return productRepository.getProductViewCountTop10();
        }

    }

}
