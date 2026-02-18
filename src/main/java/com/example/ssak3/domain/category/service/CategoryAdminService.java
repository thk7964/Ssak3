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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryAdminService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    /**
     * 카테고리 생성
     */
    @CacheEvict(value = "categoryRedisCache", allEntries = true)
    @Transactional
    public CategoryCreateResponse createCategory(CategoryCreateRequest request) {

        String name = request.getName().trim();

        duplicateCheck(name, null);

        Category category = new Category(name);

        Category savedCategory = categoryRepository.save(category);

        return CategoryCreateResponse.from(savedCategory);
    }

    /**
     * 카테고리 수정
     */
    @CacheEvict(value = "categoryRedisCache", allEntries = true)
    @Transactional
    public CategoryUpdateResponse updateCategory(Long categoryId, CategoryUpdateRequest request) {

        Category category = findCategory(categoryId);

        String name = request.getName().trim();
        duplicateCheck(name, categoryId);

        category.update(request);

        return CategoryUpdateResponse.from(category);
    }

    /**
     * 카테고리 삭제
     */
    @CacheEvict(value = "categoryRedisCache", allEntries = true)
    @Transactional
    public CategoryDeleteResponse deleteCategory(Long categoryId) {

        Category category = findCategory(categoryId);

        if (productRepository.existsByCategoryId(categoryId)) {
            throw new CustomException(ErrorCode.CATEGORY_HAS_PRODUCTS);
        }

        category.softDelete();

        return CategoryDeleteResponse.from(category);
    }

    private void duplicateCheck(String name, Long categoryId) {

        if (categoryId == null) {
            if (categoryRepository.existsByNameAndIsDeletedFalse(name)) {
                throw new CustomException(ErrorCode.CATEGORY_DUPLICATED);
            }
        } else {
            if (categoryRepository.existsByNameAndIsDeletedFalseAndIdNot(name, categoryId)) {
                throw new CustomException(ErrorCode.CATEGORY_DUPLICATED);
            }
        }
    }

    private Category findCategory(Long categoryId) {

        return categoryRepository.findByIdAndIsDeletedFalse(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
    }
}
