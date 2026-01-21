package com.example.ssak3.domain.search.repository;

import com.example.ssak3.domain.search.model.response.ProductSearchResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static com.example.ssak3.domain.product.entity.QProduct.product;
import static com.example.ssak3.domain.timedeal.entity.QTimeDeal.timeDeal;

@Repository
@RequiredArgsConstructor
public class SearchCustomRepositoryImpl implements SearchCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ProductSearchResponse> searchProduct(String keyword, Integer minPrice, Integer maxPrice, Pageable pageable) {

        Long total = queryFactory
                .select(product.countDistinct())
                .from(product)
                .leftJoin(timeDeal)
                .on(product.id.eq(timeDeal.product.id))
                .where(nameContains(keyword))
                .fetchOne();

        long totalCount = (total != null) ? total : 0L;

        return new PageImpl<>(queryFactory
                .select(Projections.constructor(ProductSearchResponse.class,
                        product.id,
                        product.name,
                        timeDeal.dealPrice.coalesce(product.price).as("price"),
                        product.information,
                        product.createdAt,
                        product.updatedAt
                ))
                .from(product)
                .leftJoin(timeDeal)
                .on(product.id.eq(timeDeal.product.id))
                .where(
                        nameContains(keyword),
                        priceGoe(minPrice),
                        priceLoe(maxPrice))
                .orderBy(product.name.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch(),
        pageable,
        totalCount);

    }

    private BooleanExpression nameContains(String keyword) {
        return (keyword != null) ? product.name.contains(keyword) : null;
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
