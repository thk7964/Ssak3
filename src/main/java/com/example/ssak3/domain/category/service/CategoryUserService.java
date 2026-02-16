package com.example.ssak3.domain.category.service;

import com.example.ssak3.domain.category.entity.Category;
import com.example.ssak3.domain.category.model.response.CategoryListGetResponse;
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
     * 카테고리 목록 조회
     */
    @Cacheable(
            cacheManager = "categoryCacheManager",
            value = "categoryRedisCache",
            key = "'all'"
    )
    @Transactional(readOnly = true)
    public List<CategoryListGetResponse> getCategoryList() {

        List<Category> categoryList = categoryRepository.findByIsDeletedFalse();

        return categoryList.stream()
                .map(CategoryListGetResponse::from)
                .toList();
    }
}
