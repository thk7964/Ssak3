package com.example.ssak3.domain.product.repository;

import com.example.ssak3.common.enums.ProductStatus;
import com.example.ssak3.common.enums.TimeDealStatus;
import com.example.ssak3.domain.product.model.response.ProductGetPopularResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.ssak3.domain.product.entity.QProduct.product;
import static com.example.ssak3.domain.timedeal.entity.QTimeDeal.timeDeal;

@Repository
@RequiredArgsConstructor
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProductGetPopularResponse> getProductViewCountTop10() {

        return queryFactory
                .select(Projections.constructor(ProductGetPopularResponse.class,
                        product.id,
                        timeDeal.id,
                        product.category.id,
                        product.name,
                        product.price,
                        timeDeal.dealPrice,
                        product.createdAt
                ))
                .from(product)
                .leftJoin(timeDeal)
                .on(
                        // 삭제되었거나 종료된 타임딜 상품일 경우 원래 상품 가격으로 조회 되도록 처리
                        product.id.eq(timeDeal.product.id),
                        timeDeal.isDeleted.eq(false),
                        timeDeal.status.eq(TimeDealStatus.OPEN),
                        timeDeal.startAt.before(LocalDateTime.now()),
                        timeDeal.endAt.after(LocalDateTime.now())
                )
                .where(
                        product.isDeleted.eq(false),
                        product.status.eq(ProductStatus.FOR_SALE)
                )
                .orderBy(product.viewCount.desc())
                .fetch();
    }
}
