package com.example.ssak3.domain.product.repository;

import com.example.ssak3.common.enums.ProductStatus;
import com.example.ssak3.domain.product.entity.Product;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.ssak3.domain.category.entity.QCategory.category;
import static com.example.ssak3.domain.product.entity.QProduct.product;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product> findProductListForAdmin(Long categoryId, ProductStatus status, Pageable pageable) {

        List<Product> list = queryFactory
                .selectFrom(product)
                .leftJoin(product.category, category).fetchJoin()
                .where(
                        product.isDeleted.eq(false),
                        categoryEq(categoryId),
                        statusEq(status)
                )
                .orderBy(product.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(product.count())
                .from(product)
                .where(
                        product.isDeleted.eq(false),
                        categoryEq(categoryId),
                        statusEq(status)
                )
                .fetchOne();

        return new PageImpl<>(list, pageable, count == null ? 0 : count);
    }

    private BooleanExpression statusEq(ProductStatus status) {
        return status != null ? product.status.eq(status) : null;
    }

    private BooleanExpression categoryEq(Long categoryId) {
        return categoryId != null ? product.category.id.eq(categoryId) : null;
    }
}