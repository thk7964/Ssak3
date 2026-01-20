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

    // 상품 목록을 가져오는데, 만약에 어떤 상품이 타임딜 상품이면 원래 상품을 보여주지 않고 그 상품에 한해서는 타임딜 상품 정보를 가져와서 보여줄거임
    // 상품 <-> 타임딜은 논리적으로 1:1 관계이지만, 현재 정책 상 타임딜이 소프트 딜리트가 적용 되어 있기 때문에 물리적으로는 N:1 관계
    // 상품 중에 타임딜로 등록될 때 바뀌는 정보가 뭐뭐 있지? 가격 뿐. 목록 조회니까 상태, 재고 이런 건 보여줄 필요가 없음 -> 그러면 가격만 타임딜 테이블에서 가져오고 나머지는 상품에서 그냥 가져오자.
    // 상품 <-> 테이블은 product_id로 참조 되어 있음
    // 1. if (상품 상태 == 타임딜) 이면 가격은 타임딜에서 가져오기 -> 그러나 현재 정책에서 타임딜 상태는 존재하지 않음
    // 2. 타임딜 테이블에 생성된 상품들은 가격을 타임딜에서 가져오기
    // 두 테이블을 조인해서...타임딜 쪽 product_id를 찾아서 그 상품은 타임딜에 등록된 상품이므로 가격을 타임딜 테이블에서 가져오자.
    // 어떤 조인할까? 타임딜은 없고 상품만 있어도 상품이 다 조회 되어야 하기 때문에 상품 기준으로 left join 하자.
    // 이래서 DTO를 쓰는 건가? DTO 덕분에 원하는 값만 골라서 가져오기 수월했다.

    @Override
    public Page<ProductSearchResponse> searchProduct(String keyword, Pageable pageable) {

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
                .where(nameContains(keyword))
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


}
