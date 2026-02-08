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

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductRankingService {

    private final StringRedisTemplate redisTemplate;
    private static final String PRODUCT_DAILY_RANKING_PREFIX = "product:ranking:";
    private static final String PRODUCT_WEEKLY_RANKING_KEY = "product:ranking:weekly";
    private static final String PRODUCT_VIEW_CHECK_PREFIX = "product:view:check:ip:";
    private final ProductRepository productRepository;
    private final TimeDealRepository timeDealRepository;

    /**
     * 조회수 증가 메소드
     */
    public void increaseViewCount(Long productId, String ip) {

        LocalDateTime now = LocalDateTime.now();

        String key = PRODUCT_VIEW_CHECK_PREFIX + ip + ":productId:" + productId; // 오늘 조회 했는지 체크할 Key
        LocalDateTime midnight = LocalDate.now().plusDays(1).atStartOfDay(); // 다음 날 자정 만료
        Boolean isFirstView = redisTemplate.opsForValue().setIfAbsent(key, "1", Duration.between(now, midnight).getSeconds(), TimeUnit.SECONDS);

        // 오늘 첫 조회가 아니면 조회수 증가 x
        if (Boolean.FALSE.equals(isFirstView)) {
            return;
        }

        LocalDate nowDay =  LocalDate.now();

        Double score = redisTemplate.opsForZSet().incrementScore(PRODUCT_DAILY_RANKING_PREFIX + nowDay, productId.toString(), 1);

        // 최초 한 번만 TTL 설정
        if (score != null && score == 1.0) {

            // 오늘로부터 10일 뒤 자정 시점 구하기
            LocalDateTime dayViewCountExp = nowDay.plusDays(10).atStartOfDay();

            // TTL 설정: Redis에서 오늘로부터 10일 뒤 데이터 만료
            redisTemplate.expireAt(PRODUCT_DAILY_RANKING_PREFIX + nowDay, dayViewCountExp.atZone(ZoneId.systemDefault()).toInstant());
        }

    }

    /**
     * 조회수 TOP 10 상품 조회
     */
    public List<ProductGetPopularResponse> getPopularProductTop10() {

        LocalDate now = LocalDate.now();

        List<String> otherKeys = new ArrayList<>();

        // 현재 시점으로부터 일주일치 키 리스트 생성 (오늘은 제외)
        for (int i = 0; i < 6; i++) {
            otherKeys.add(PRODUCT_DAILY_RANKING_PREFIX + now.minusDays(i + 1));
        }

        // 일주일치 결과 집계
        redisTemplate.opsForZSet().unionAndStore(
                PRODUCT_DAILY_RANKING_PREFIX + now,
                    otherKeys,
                    PRODUCT_WEEKLY_RANKING_KEY);

        // 오늘로부터 10일 뒤 자정 시점 구하기
        LocalDateTime expirationTime = now.plusDays(10).atStartOfDay();

        // TTL 설정: 덮어씌워질 때마다 만료 시간을 10일 뒤로 갱신
        redisTemplate.expireAt(PRODUCT_WEEKLY_RANKING_KEY, expirationTime.atZone(ZoneId.systemDefault()).toInstant());

        // 랭킹으로 조회
        Set<ZSetOperations.TypedTuple<String>> result = redisTemplate.opsForZSet().reverseRangeWithScores(PRODUCT_WEEKLY_RANKING_KEY, 0, 9);

        if (result == null || result.isEmpty()) {
            return Collections.emptyList();
        }

        // DB에서 TOP 10 상품 id 리스트 가져오기: Redis가 정렬해준 순서 보장함
        List<Long> productIdList = result.stream().map(id -> Long.parseLong(id.getValue())).toList();

        // DB에서 Product 가져오기: in 연산은 Redis가 정렬해준 순서를 보장해주지 않음
        List<Product> productList = productRepository.findAllByIdInAndStatusAndIsDeletedFalse(productIdList, ProductStatus.FOR_SALE);

        // DB에서 TimeDeal 중인 상품 있으면 가져오기
        List<TimeDeal> timeDealList = timeDealRepository.findAllByProductIdInAndStatusAndIsDeletedFalse(productIdList, TimeDealStatus.OPEN);

        Map<Long, Product> productMap = productList.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        Map<Long, TimeDeal> timeDealMap = timeDealList.stream()
                .collect(Collectors.toMap(timeDeal -> timeDeal.getProduct().getId(), timeDeal -> timeDeal));

        // 랭킹에 따라 조립
        return productIdList.stream()
                .map(id -> {
                    Product product = productMap.get(id);
                    return product != null ? ProductGetPopularResponse.from(product, timeDealMap.get(id)) : null;
                })
                .filter(Objects::nonNull) // null인 애들은 걸러냄
                .toList();
    }

}
