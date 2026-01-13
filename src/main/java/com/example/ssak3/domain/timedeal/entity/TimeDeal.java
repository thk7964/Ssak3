package com.example.ssak3.domain.timedeal.entity;

import com.example.ssak3.common.entity.BaseEntity;
import com.example.ssak3.domain.product.entity.Product;
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

    @Column(nullable = false, name = "deal_price")
    private Integer dealPrice;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false, name = "start_at")
    private LocalDateTime startAt;

    @Column(nullable = false, name = "end_at")
    private LocalDateTime endAt;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted;
}
