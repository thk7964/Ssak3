package com.example.ssak3.domain.product.repository;

import com.example.ssak3.common.enums.ProductStatus;
import com.example.ssak3.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductCustomRepository {
    Page<Product> findProductListForAdmin(Long categoryId, ProductStatus status, Pageable pageable);
}