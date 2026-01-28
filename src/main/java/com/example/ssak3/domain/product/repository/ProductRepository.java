package com.example.ssak3.domain.product.repository;

import com.example.ssak3.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductCustomRepository {

    Optional<Product> findByIdAndIsDeletedFalse(Long id);


    @Query("""
    SELECT p
    FROM Product p
    WHERE p.isDeleted = false
      AND (:categoryId IS NULL OR p.category.id = :categoryId)
      AND p.status IN (
      com.example.ssak3.common.enums.ProductStatus.FOR_SALE,
      com.example.ssak3.common.enums.ProductStatus.SOLD_OUT)
""")
        // 반환타입을 Page로 수정
    Page<Product> findProductListByCategoryId(
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );

    List<Product> findByIdIn(List<Long> productIdList);

    @Query("""
    SELECT p
    FROM Product p
    WHERE p.isDeleted = false
      AND (:categoryId IS NULL OR p.category.id = :categoryId)
""")
        // 반환타입을 Page로 수정
    Page<Product> findProductListByCategoryIdForAdmin(
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );

    // 카테고리 아이디를 통해 상품의 존재확인
    boolean existsByCategoryId(Long categoryId);
}
