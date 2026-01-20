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

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted;

    public TimeDeal(Product product, Integer dealPrice, LocalDateTime startAt, LocalDateTime endAt) {
        this.product = product;
        this.dealPrice = dealPrice;
        this.startAt = startAt;
        this.endAt = endAt;
        this.isDeleted = false;
    }

    /*
     *  현재 시점 기준으로 타임딜의 상태를 반환
     * */
    public TimeDealStatus getStatus(LocalDateTime now) {
        if (isDeleted) return TimeDealStatus.DELETED;
        if (now.isBefore(startAt)) {open(); return TimeDealStatus.READY;}
        if (now.isAfter(endAt)) {
            open();
            return TimeDealStatus.CLOSED;
        }
        closed();
        return TimeDealStatus.OPEN;
    }

    public void softDelete() {
        this.isDeleted = true;
    }

    public void update(TimeDealUpdateRequest request) {

        if (request.getDealPrice() != null) {
            this.dealPrice = request.getDealPrice();
        }
        if (request.getStartAt() != null) {
            this.startAt = request.getStartAt();
        }
        if (request.getEndAt() != null) {
            this.endAt = request.getEndAt();
        }
    }

    public boolean isDeletable(LocalDateTime now) {
        TimeDealStatus status = getStatus(now);
        return status == TimeDealStatus.READY || status == TimeDealStatus.CLOSED;
    }

    public void open() {
        if (isDeleted) return;
        product.stopSaleForTimeDeal();
    }

    public void closed() {
        if (isDeleted) return;
        product.restoreStatusAfterTimeDeal();
    }


}
