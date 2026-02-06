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

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted;

    public TimeDeal(Product product, Integer dealPrice, LocalDateTime startAt, LocalDateTime endAt) {
        this.product = product;
        this.dealPrice = dealPrice;
        this.startAt = startAt;
        this.endAt = endAt;
        this.isDeleted = false;
        this.status = TimeDealStatus.READY;
    }

    public void setStatus(TimeDealStatus newStatus) {

        if (this.status == newStatus) return;

        this.status = newStatus;

        if (newStatus == TimeDealStatus.OPEN) {
            closeNormalProduct();
        }else {
            openNormalProduct();
        }

    }

    public void softDelete() {
        this.isDeleted = true;
        this.status = TimeDealStatus.DELETED;
    }

    public void update(TimeDealUpdateRequest request) {

        if (request.getDealPrice() != null) {
            this.dealPrice = request.getDealPrice();
        }
        if (request.getStartAt() != null) {
            this.startAt = request.getStartAt();
            setStatus(TimeDealStatus.READY);
        }
        if (request.getEndAt() != null) {
            this.endAt = request.getEndAt();
        }
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
