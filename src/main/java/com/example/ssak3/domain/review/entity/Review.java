package com.example.ssak3.domain.review.entity;

import com.example.ssak3.common.entity.BaseEntity;
import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.review.model.request.ReviewUpdateRequest;
import com.example.ssak3.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String content;

    @Column(nullable = false)
    private Integer score;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted = false;

    public Review(User user, Product product, String content, Integer score) {
        this.user = user;
        this.product = product;
        this.content = content;
        this.score = score;
    }

    public void update(ReviewUpdateRequest request) {
        this.content = request.getContent();
        this.score = request.getScore();
    }

    public void softDelete() {
        this.isDeleted = true;
    }

}
