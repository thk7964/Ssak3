package com.example.ssak3.domain.product.service;

import com.example.ssak3.common.enums.ProductStatus;
import com.example.ssak3.common.enums.TimeDealStatus;
import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.product.model.response.ProductGetPopularResponse;
import com.example.ssak3.domain.product.repository.ProductRepository;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import com.example.ssak3.domain.timedeal.repository.TimeDealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductRankingService {

    private final StringRedisTemplate redisTemplate;
    private static final String PRODUCT_DAILY_RANKING_PREFIX = "product:ranking:";
    private final ProductRepository productRepository;
    private final TimeDealRepository timeDealRepository;

    public void increaseViewCount(Long productId) {

        redisTemplate.opsForZSet().incrementScore(PRODUCT_DAILY_RANKING_PREFIX + LocalDate.now(), productId.toString(), 1);
    }

    public List<ProductGetPopularResponse> getPopularProductTop10() {

        Set<ZSetOperations.TypedTuple<String>> result = redisTemplate.opsForZSet().reverseRangeWithScores(PRODUCT_DAILY_RANKING_PREFIX + LocalDate.now(), 0, 9);

        if (result == null || result.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> productIdList = result.stream().map(tuple -> Long.parseLong(tuple.getValue())).toList();

        List<Product> productList = productRepository.findAllByIdInAndStatusAndIsDeletedFalse(productIdList, ProductStatus.FOR_SALE);

        List<TimeDeal> timeDealList = timeDealRepository.findAllByProductIdInAndStatusAndIsDeletedFalse(productIdList, TimeDealStatus.OPEN);

        Map<Long, Product> productMap = productList.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        Map<Long, TimeDeal> timeDealMap = timeDealList.stream()
                .collect(Collectors.toMap(timeDeal -> timeDeal.getProduct().getId(), timeDeal -> timeDeal));

        return productIdList.stream()
                .map(id -> {
                    Product product = productMap.get(id);
                    return product != null ? ProductGetPopularResponse.from(product, timeDealMap.get(id)) : null;
                })
                .filter(Objects::nonNull)
                .toList();
    }

}
