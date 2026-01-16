package com.example.ssak3.domain.category.repository;

import com.example.ssak3.domain.category.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByIdAndIsDeletedFalse(Long id);

    @Query("""
            SELECT c
            FROM Category c
            WHERE c.isDeleted = false
            """)
    Page<Category> findCategoryPage(
            Pageable pageable
    );
}
