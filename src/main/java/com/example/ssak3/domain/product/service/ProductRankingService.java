package com.example.ssak3.domain.product.service;

import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.product.model.response.ProductGetPopularResponse;
import com.example.ssak3.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductRankingService {

    private final StringRedisTemplate redisTemplate;
    private final ProductRepository productRepository;
    private static final String PRODUCT_VIEW_RANKING = "product:ranking:view";

    /**
     * 조회수 증가
     */
    public void increaseViewCount(Long productId) {

        redisTemplate.opsForZSet().incrementScore(PRODUCT_VIEW_RANKING, productId.toString(), 1);
    }

    /**
     * 상품 조회수 TOP 10 조회
     */
    public List<ProductGetPopularResponse> getPopularProductTop10() {

        Set<ZSetOperations.TypedTuple<String>> result = redisTemplate.opsForZSet().reverseRangeWithScores(PRODUCT_VIEW_RANKING, 0, 9);

        if (result == null || result.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> productIdList = result.stream().map(tuple -> Long.parseLong(tuple.getValue())).toList();

        List<Product> productList = productRepository.findByIdIn(productIdList);

        Map<Long, Product> productMap = productList.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        List<ProductGetPopularResponse> responseList = new ArrayList<>();

        for (Long productId : productIdList) {
            Product product = productMap.get(productId);

            if (product != null) {
                responseList.add(ProductGetPopularResponse.from(product));
            }
        }

        return responseList;
    }

}