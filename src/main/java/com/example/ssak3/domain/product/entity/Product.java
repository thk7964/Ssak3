package com.example.ssak3.domain.product.entity;

import com.example.ssak3.common.entity.BaseEntity;
import com.example.ssak3.domain.category.entity.Category;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String information;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted = false;

    public Product(String name, Integer price, String status, String information, Integer quantity) {
        this.name = name;
        this.price = price;
        this.status = status;
        this.information = information;
        this.quantity = quantity;
    }
}
