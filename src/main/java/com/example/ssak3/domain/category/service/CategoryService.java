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
import com.example.ssak3.domain.user.repository.UserRepository;
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
    private final UserRepository userRepository;

    /**
     * 카테고리 생성 비즈니스 로직
     */
    @Transactional
    public CategoryCreateResponse createCategory(AuthUser user, CategoryCreateRequest request) {
        // 관리자의 존재여부 확인
        userRepository.findByIdAndIsDeletedFalse(user.getId())
                .orElseThrow(()-> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

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
    public CategoryUpdateResponse updateCategory(AuthUser user, Long categoryId, CategoryUpdateRequest request) {
        userRepository.findByIdAndIsDeletedFalse(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

        Category findCategory = categoryRepository.findByIdAndIsDeletedFalse(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        findCategory.update(request);

       return CategoryUpdateResponse.form(findCategory);
    }

    /**
     * 카테고리 삭제 비즈니스 로직
     */
    @Transactional
    public CategoryDeleteResponse deleteCategory(AuthUser user, Long categoryId) {
        userRepository.findByIdAndIsDeletedFalse(user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

        Category findCategory = categoryRepository.findByIdAndIsDeletedFalse(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        findCategory.softDelete();

        return CategoryDeleteResponse.form(findCategory);
    }
}
