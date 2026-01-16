package com.example.ssak3.domain.product.entity;

import com.example.ssak3.common.entity.BaseEntity;
import com.example.ssak3.common.enums.ProductStatus;
import com.example.ssak3.domain.category.entity.Category;
import com.example.ssak3.domain.product.model.request.ProductUpdateRequest;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    @Column(nullable = false)
    private String information;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted = false;

    public Product(Category category, String name, Integer price, ProductStatus status, String information, Integer quantity) {
        this.category = category;
        this.name = name;
        this.price = price;
        this.status = status;
        this.information = information;
        this.quantity = quantity;
    }

    public void decreaseQuantity(Integer orderProductQuantity) {
        this.quantity -= orderProductQuantity;
    }

    public void update(ProductUpdateRequest request) {

        if (request.getName() != null) {
            this.name = request.getName();
        }
        if (request.getPrice() != null) {
            this.price = request.getPrice();
        }
        if (request.getStatus() != null) {
            this.status = request.getStatus();
        }
        if (request.getInformation() != null) {
            this.information = request.getInformation();
        }
        if (request.getQuantity() != null) {
            this.quantity = request.getQuantity();
        }
    }

    public void isDeleted() {
        this.isDeleted = true;
    }

}
