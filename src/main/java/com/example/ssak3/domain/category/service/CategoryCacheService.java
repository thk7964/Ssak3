//package com.example.ssak3.domain.category.service;
//
//package com.example.ssak3.domain.category.service;
//
//
//import com.example.ssak3.common.enums.ErrorCode;
//import com.example.ssak3.common.exception.CustomException;
//import com.example.ssak3.domain.category.entity.Category;
//import com.example.ssak3.domain.category.model.response.CategoryCommonResponse;
//import com.example.ssak3.domain.category.repository.CategoryRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.jspecify.annotations.NonNull;
//import org.springframework.cache.Cache;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.CachePut;
//import org.springframework.cache.caffeine.CaffeineCache;
//import org.springframework.stereotype.Component;
//
//import java.util.Collection;
//import java.util.Objects;
//import java.util.concurrent.ConcurrentMap;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class CategoryCacheService {
//
//    private final CacheManager cacheManager;
//    private final CategoryRepository categoryRepository;
//
//    /**
//     * 카테고리 캐시
//     */
//    public Collection<@NonNull Object> getCacheValue() {
//        Cache categoryCache = cacheManager.getCache("categoryCache");
//        CaffeineCache caffeineCache = (CaffeineCache) categoryCache;
//        ConcurrentMap<Object, @NonNull Object> map = Objects.requireNonNull(caffeineCache).getNativeCache().asMap();
//        Collection<@NonNull Object> values = Objects.requireNonNull(caffeineCache).getNativeCache().asMap().values();
//        log.info("Cache에서 데이터 조회시 map에 담긴 정보 {}", map);
//        return values;
//        //        log.info("service Cache에서 조회된 결과: {}", values);
//    }
//
//    /**
//     * 카테고리 캐시 저장
//     */
//    public void saveCache(Long keyId, Object value) {
//        Cache categoryCache = cacheManager.getCache("categoryCache");
//        categoryCache.put(keyId, value);
//    }
//
//    /**
//     * 카테고리 캐시 수정
//     */
//    @CachePut(value = "categoryCache", key = "#categoryId")
//    public CategoryCommonResponse updateCache(Long categoryId) {
//        // 업데이트할 데이터 반환
//        Category category = categoryRepository.findByIdAndIsDeletedFalse(categoryId)
//                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
//        return CategoryCommonResponse.from(category);
//    }
//
//    /**
//     * 카테고리 캐시 삭제
//     */
//    @CacheEvict(value = "categoryCache", key = "#categoryId")
//    public CategoryCommonResponse deletedCache(Long categoryId, Category category) {
//
//        return CategoryCommonResponse.from(category);
//    }
//}