package com.example.ssak3.domain.review.repository;

import com.example.ssak3.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByIdAndIsDeletedFalse(Long id);

    Page<Review> findByProductIdAndIsDeletedFalse(Long productId, Pageable pageable);

    @Query("""
    select avg(r.score)
    from Review r
    where r.isDeleted = false
    and r.product.id = :productId
""")
    Double findAverageScoreByProductId(Long productId);
}
