package com.example.ssak3.domain.timedeal.entity;

import com.example.ssak3.common.entity.BaseEntity;
import com.example.ssak3.common.enums.TimeDealStatus;
import com.example.ssak3.domain.product.entity.Product;
import com.example.ssak3.domain.timedeal.model.request.TimeDealUpdateRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "time_deals")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeDeal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "deal_price", nullable = false)
    private Integer dealPrice;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TimeDealStatus status;

    @Column(columnDefinition = "TEXT")
    private String image;

    @Column(columnDefinition = "TEXT", name = "detail_image")
    private String detailImage;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted;

    public TimeDeal(Product product, Integer dealPrice, LocalDateTime startAt, LocalDateTime endAt, String image, String detailImage) {
        this.product = product;
        this.dealPrice = dealPrice;
        this.startAt = startAt;
        this.endAt = endAt;
        this.isDeleted = false;
        this.status = TimeDealStatus.READY;
        this.image = image;
        this.detailImage = detailImage;
    }

    public void setStatus(TimeDealStatus newStatus) {

        if (this.status == newStatus) return;

        this.status = newStatus;

        if (newStatus == TimeDealStatus.OPEN) {
            closeNormalProduct();
        } else {
            openNormalProduct();
        }
    }

    public void softDelete() {

        this.isDeleted = true;
        this.status = TimeDealStatus.DELETED;

        if (this.image != null) {
            this.image = null;
        }

        if (this.detailImage != null) {
            this.detailImage = null;
        }
    }

    public void update(TimeDealUpdateRequest request) {

        boolean startAtChanged = false;

        if (request.getDealPrice() != null) {
            this.dealPrice = request.getDealPrice();
        }

        if (request.getStartAt() != null && !request.getStartAt().equals(this.startAt)) {
            this.startAt = request.getStartAt();
            startAtChanged = true;
        }

        if (request.getEndAt() != null) {
            this.endAt = request.getEndAt();
        }

        if (request.getImage() != null) {
            this.image = request.getImage();
        }

        if (request.getDetailImage() != null) {
            this.detailImage = request.getDetailImage();
        }

        if (startAtChanged) {
            prepareIfPossible();
        }
    }

    public void prepareIfPossible() {

        if (this.product.getQuantity() <= 0) {
            return;
        }

        if (this.status == TimeDealStatus.READY) {
            return;
        }

        this.status = TimeDealStatus.READY;
        openNormalProduct();
    }

    public boolean isDeletable() {
        return status == TimeDealStatus.READY || status == TimeDealStatus.CLOSED;
    }

    private void openNormalProduct() {

        if (isDeleted) return;

        product.restoreStatusAfterTimeDeal();
    }

    private void closeNormalProduct() {

        if (isDeleted) return;

        product.stopSaleForTimeDeal();
    }
}
