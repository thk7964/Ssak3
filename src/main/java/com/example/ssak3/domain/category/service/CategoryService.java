package com.example.ssak3.domain.category.service;

import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.exception.CustomException;
import com.example.ssak3.common.model.AuthUser;
import com.example.ssak3.common.model.PageResponse;
import com.example.ssak3.domain.category.entity.Category;
import com.example.ssak3.domain.category.model.request.CategoryCreateRequest;
import com.example.ssak3.domain.category.model.request.CategoryUpdateRequest;
import com.example.ssak3.domain.category.model.response.*;
import com.example.ssak3.domain.category.repository.CategoryRepository;
import com.example.ssak3.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

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
     * 카테고리 목록조회 비즈니스 로직
     */
    @Transactional(readOnly = true)
    public PageResponse<CategoryListGetResponse> getCategoryList(Pageable pageable) {
        Page<CategoryListGetResponse> categoryPage = categoryRepository.findCategoryPage(pageable)
                .map(CategoryListGetResponse::from);
        return PageResponse.from(categoryPage);
    }

    /**
     * 카테고리 수정 비즈니스 로직
     */
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
