package com.example.ssak3.domain.product.repository;

import com.example.ssak3.common.enums.ProductStatus;
import com.example.ssak3.common.enums.TimeDealStatus;
import com.example.ssak3.domain.product.model.response.ProductGetSearchResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import static com.example.ssak3.domain.product.entity.QProduct.product;
import static com.example.ssak3.domain.timedeal.entity.QTimeDeal.timeDeal;

@Repository
@RequiredArgsConstructor
public class ProductSearchCustomRepositoryImpl implements ProductSearchCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ProductGetSearchResponse> searchProduct(String keyword, Integer minPrice, Integer maxPrice, Pageable pageable) {

        Long total = queryFactory
                .select(product.countDistinct())
                .from(product)
                .leftJoin(timeDeal)
                .on(
                        product.id.eq(timeDeal.product.id),
                        timeDeal.isDeleted.eq(false),
                        timeDeal.status.eq(TimeDealStatus.OPEN),
                        timeDeal.startAt.before(LocalDateTime.now()),
                        timeDeal.endAt.after(LocalDateTime.now())
                )
                .where(
                        product.isDeleted.eq(false),
                        product.status.eq(ProductStatus.FOR_SALE),
                        nameContains(keyword),
                        priceGoe(minPrice),
                        priceLoe(maxPrice)
                )
                .fetchOne();

        long totalCount = (total != null) ? total : 0L;

        return new PageImpl<>(queryFactory
                .select(Projections.constructor(ProductGetSearchResponse.class,
                        product.id,
                        timeDeal.id,
                        product.category.id,
                        product.name,
                        product.information,
                        product.price,
                        timeDeal.dealPrice,
                        product.image,
                        product.createdAt,
                        product.updatedAt
                ))
                .from(product)
                .leftJoin(timeDeal)
                .on(
                        product.id.eq(timeDeal.product.id),
                        timeDeal.isDeleted.eq(false),
                        timeDeal.status.eq(TimeDealStatus.OPEN),
                        timeDeal.startAt.before(LocalDateTime.now()),
                        timeDeal.endAt.after(LocalDateTime.now())
                        )
                .where(
                        // LEFT JOIN은 왼쪽 테이블에 어떤 조건을 걸어도 조인 실패 시 오른쪽 테이블 행에 매핑을 실패할 뿐, 왼쪽 테이블 행이 필터링 되지 않음
                        // LEFT JOIN에서 ON 절은 오른쪽 테이블을 갖다 붙일 자격만 따짐 -> 따라서 Product 조건은 WHERE 절에 걸어줌
                        product.isDeleted.eq(false),
                        product.status.eq(ProductStatus.FOR_SALE),
                        nameContains(keyword),
                        priceGoe(minPrice),
                        priceLoe(maxPrice)
                )
                .orderBy(product.price.asc(), timeDeal.dealPrice.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch(),
        pageable,
        totalCount);

    }

    private BooleanExpression nameContains(String keyword) {
        return (keyword != null && !keyword.isBlank()) ? product.name.contains(keyword) : null;
    }

    private BooleanExpression priceGoe(Integer minPrice) {

        if (minPrice == null) return null;

        return timeDeal.dealPrice.coalesce(product.price).goe(minPrice);
    }

    private BooleanExpression priceLoe(Integer maxPrice) {

        if (maxPrice == null) return null;

        return timeDeal.dealPrice.coalesce(product.price).loe(maxPrice);
    }
}
