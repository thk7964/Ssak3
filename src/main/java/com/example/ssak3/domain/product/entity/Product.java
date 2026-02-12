package com.example.ssak3.domain.product.entity;

import com.example.ssak3.common.entity.BaseEntity;
import com.example.ssak3.common.enums.ErrorCode;
import com.example.ssak3.common.enums.ProductStatus;
import com.example.ssak3.common.exception.CustomException;
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

    @Column
    private String image;

    @Column(name = "detail_image")
    private String detailImage;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted = false;

    public Product(Category category, String name, Integer price, ProductStatus status, String information, Integer quantity, String image, String detailImage) {
        this.category = category;
        this.name = name;
        this.price = price;
        this.status = status;
        this.information = information;
        this.quantity = quantity;
        this.image = image;
        this.detailImage = detailImage;
    }

    public void decreaseQuantity(Integer orderProductQuantity) {
        if (this.quantity < orderProductQuantity) {
            throw new CustomException(ErrorCode.PRODUCT_INSUFFICIENT);
        }
        this.quantity -= orderProductQuantity;

        if (this.quantity == 0) {
            this.status = ProductStatus.SOLD_OUT;
        }
    }

    public void rollbackQuantity(Integer quantity) {
        if (quantity <= 0) {
            throw new CustomException(ErrorCode.INVALID_ROLLBACK_QUANTITY);
        }

        this.quantity += quantity;
        if (this.quantity > 0 && this.status == ProductStatus.SOLD_OUT) {
            this.status = ProductStatus.FOR_SALE;
        }
    }

    public void update(ProductUpdateRequest request, Category category) {

        this.category = category;

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
        if (request.getImage() != null) {
            this.image = request.getImage();
        }
        if (request.getDetailImage() != null) {
            this.detailImage = request.getDetailImage();
        }
    }

    public void softDelete() {
        this.isDeleted = true;

        if (this.image != null) {
            this.image = null;
        }

        if (this.detailImage != null) {
            this.detailImage = null;
        }
    }

    public void stopSaleForTimeDeal() {
        if (status!= ProductStatus.STOP_SALE){
            status= ProductStatus.STOP_SALE;
        }
    }

    public void restoreStatusAfterTimeDeal() {
        if (status!= ProductStatus.FOR_SALE){
            status=ProductStatus.FOR_SALE;
        }
    }

    public void updateStatus(ProductStatus status) {
        this.status = status;
    }

}
