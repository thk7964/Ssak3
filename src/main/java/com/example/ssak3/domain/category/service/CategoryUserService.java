package com.example.ssak3.domain.category.service;

import com.example.ssak3.domain.category.entity.Category;
import com.example.ssak3.domain.category.model.response.*;
import com.example.ssak3.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryUserService {

    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 목록조회 비즈니스 로직
     */
    @Cacheable(cacheManager = "categoryCacheManager",
            value = "categoryRedisCache",
            key = "'all'")
    @Transactional(readOnly = true)
    public List<CategoryListGetResponse> getCategoryList() {
        List<Category> categoryList = categoryRepository.findByIsDeletedFalse();
        List<CategoryListGetResponse> listGetResponse = categoryList.stream().map(CategoryListGetResponse::from).toList();
        log.info("service DB에서 조회된 결과: {}", listGetResponse);
        return listGetResponse;
    }
}
