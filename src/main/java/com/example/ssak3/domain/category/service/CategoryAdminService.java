package com.example.ssak3.domain.category.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.domain.category.entity.Category;
import com.example.ssak3.domain.category.model.request.CategoryCreateRequest;
import com.example.ssak3.domain.category.model.request.CategoryUpdateRequest;
import com.example.ssak3.domain.category.model.response.CategoryCreateResponse;
import com.example.ssak3.domain.category.model.response.CategoryDeleteResponse;
import com.example.ssak3.domain.category.model.response.CategoryUpdateResponse;
import com.example.ssak3.domain.category.repository.CategoryRepository;
import com.example.ssak3.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryAdminService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

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
     * 카테고리 수정 비즈니스 로직
     */
    @CacheEvict(value = "categoryRedisCache", allEntries = true)
    @Transactional
    public CategoryUpdateResponse updateCategory(Long categoryId, CategoryUpdateRequest request) {
        Category findCategory = categoryRepository.findByIdAndIsDeletedFalse(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        findCategory.update(request);
        return CategoryUpdateResponse.from(findCategory);
    }

    /**
     * 카테고리 삭제 비즈니스 로직
     */
    @CacheEvict(value = "categoryRedisCache", allEntries = true)
    @Transactional
    public CategoryDeleteResponse deleteCategory(Long categoryId) {
        // 카테고리 존재여부 확인
        Category findCategory = categoryRepository.findByIdAndIsDeletedFalse(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        // 카테고리에 상품이 있다면 삭제 불가능 예외처리
        if (productRepository.existsByCategoryId(categoryId)) {
            throw new CustomException(ErrorCode.CATEGORY_HAS_PRODUCTS);
        }
        findCategory.softDelete();
        return CategoryDeleteResponse.from(findCategory);
    }
}
