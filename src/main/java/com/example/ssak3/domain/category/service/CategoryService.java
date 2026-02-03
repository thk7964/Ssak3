package com.example.ssak3.domain.category.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.category.entity.Category;
import com.example.ssak3.domain.category.model.request.CategoryCreateRequest;
import com.example.ssak3.domain.category.model.request.CategoryUpdateRequest;
import com.example.ssak3.domain.category.model.response.*;
import com.example.ssak3.domain.category.repository.CategoryRepository;
import com.example.ssak3.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CacheManager cacheManager;
    private final CategoryCacheService categoryCacheService;

    /**
     * 카테고리 생성 비즈니스 로직
     */
    @Transactional
    public CategoryCreateResponse createCategory(CategoryCreateRequest request) {

        Category category = new Category(request.getName());
        Category savedCategory = categoryRepository.save(category);
        return CategoryCreateResponse.from(savedCategory);
    }

    /**
     * 카테고리 목록조회 비즈니스 로직
     */

    @Transactional(readOnly = true)
    public List<CategoryCommonResponse> getCategoryList() {
        // Cache에 데이터가 있는지 확인
        // Cache데이터를 가져오려면 Cache데이터 구조를 내가 어떻게 정했는가 파악해야함
        // Cache데이터에 key값 하나당 하나의 데이터를 저장하게 설계
        // Cache데이터 조회를 할 때 모든 key값을 찾아서 하나씩 존재여부를 확인해야할까?
        // Cache는 쿼리메서드처럼 getAll과 같은 기능이 없는걸까 -> 없다..
        // 해결책 : 전체 리스트를 하나의 키에 저장

        // 캐시 데이터 조회
        Collection<@NonNull Object> cacheValue = categoryCacheService.getCacheValue();

        // 존재 시 반환
        if (!cacheValue.isEmpty()) {
            List<CategoryCommonResponse> categoryCommonResponse = cacheValue.stream().map(data -> (CategoryCommonResponse) data).toList();
            return categoryCommonResponse;
        }

        // cache안에 데이터가 없을때 -> DB에서 카테고리 조회
        List<Category> categoryList = categoryRepository.findByIsDeletedFalse();
        List<CategoryCommonResponse> listGetResponses = categoryList.stream().map(CategoryCommonResponse::from).toList();
        log.info("service DB에서 조회된 결과: {}", listGetResponses);

        // Cache에 key , value값 할당 후 저장
        for (CategoryCommonResponse response : listGetResponses) {
            categoryCacheService.saveCache(response.getId(), response);
        }
        return listGetResponses;
    }

    /**
     * 카테고리 수정 비즈니스 로직
     */
    @Transactional
    public CategoryUpdateResponse updateCategory(Long categoryId, CategoryUpdateRequest request) {
        log.info("service 카테고리 수정전 Id: {}", categoryId);
        Category findCategory = categoryRepository.findByIdAndIsDeletedFalse(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        findCategory.update(request);
        log.info("service 카테고리 수정후 Id: {}", findCategory.getId());
        categoryCacheService.updateCache(findCategory.getId());
       return CategoryUpdateResponse.from(findCategory);
    }

    /**
     * 카테고리 삭제 비즈니스 로직
     */
    @Transactional
    public CategoryDeleteResponse deleteCategory(Long categoryId) {

        // 카테고리 존재여부 확인
        Category findCategory = categoryRepository.findByIdAndIsDeletedFalse(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        log.info("service 카테고리 삭제전 Id: {}", findCategory.getId());
        // 카테고리에 상품이 있다면 삭제 불가능 예외처리
        if (productRepository.existsByCategoryId(categoryId)) {
            throw new CustomException(ErrorCode.CATEGORY_HAS_PRODUCTS);
        }
        findCategory.softDelete();
        log.info("service 카테고리 삭제후 Id: {}", findCategory.getId());
        CategoryCommonResponse categoryCommonResponse = categoryCacheService.deletedCache(findCategory.getId(), findCategory);
        log.info("service 카테고리 삭제후 Cache 메모리 상태: {}", categoryCommonResponse);
        return CategoryDeleteResponse.from(findCategory);
    }
}
