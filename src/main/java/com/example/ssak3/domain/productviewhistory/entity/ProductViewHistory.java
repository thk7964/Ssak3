package com.example.ssak3.domain.productviewhistory.entity;

import com.example.ssak3.common.entity.BaseEntity;
import com.example.ssak3.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@Table(
        name = "product_view_histories",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_product_id_view_date",
                        columnNames = { "product_id", "view_date" }
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductViewHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "view_date", nullable = false)
    private LocalDate viewDate;

    @Column(name = "view_count", nullable = false)
    private Integer viewCount;

    public ProductViewHistory(Product product, LocalDate viewDate, Integer viewCount) {
        this.product = product;
        this.viewDate = viewDate;
        this.viewCount = viewCount;
    }

}
