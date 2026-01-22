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
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByIdAndIsDeletedFalse(Long id);


    @Query("""
    SELECT p
    FROM Product p
    WHERE p.isDeleted = false
      AND (:categoryId IS NULL OR p.category.id = :categoryId)
""")
        // 반환타입을 Page로 수정
    Page<Product> findProductListByCategoryId(
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );

    @Query("""
    SELECT p
    FROM Product p
    WHERE p.isDeleted = false
    ORDER BY p.viewCount desc
    LIMIT 10
    """)
    List<Product> getPopularTop10();

    List<Product> findByIdIn(List<Long> productIdList);
}
