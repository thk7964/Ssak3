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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryUserService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    /**
     * 카테고리 목록조회 비즈니스 로직
     */
    @Cacheable(cacheManager = "categoryCacheManager",
            value = "categoryRedisCache",
            key = "'all'")
    @Transactional(readOnly = true)
    public List<CategoryGetResponse> getCategoryList() {
        List<Category> categoryList = categoryRepository.findByIsDeletedFalse();
        List<CategoryGetResponse> listGetResponse = categoryList.stream().map(CategoryGetResponse::from).toList();
        log.info("service DB에서 조회된 결과: {}", listGetResponse);
        return listGetResponse;
    }
}
