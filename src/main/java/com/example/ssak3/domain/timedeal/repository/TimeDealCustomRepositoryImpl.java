package com.example.ssak3.domain.timedeal.repository;

import com.example.ssak3.common.enums.TimeDealStatus;
import com.example.ssak3.domain.timedeal.model.response.TimeDealListGetResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.ssak3.domain.timedeal.entity.QTimeDeal.timeDeal;

@RequiredArgsConstructor
public class TimeDealCustomRepositoryImpl implements TimeDealCustomRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<TimeDealListGetResponse> findOpenTimeDeals(Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        List<TimeDealListGetResponse> list =
                queryFactory.select(Projections.constructor(
                                TimeDealListGetResponse.class,
                                timeDeal.id,
                                timeDeal.product.name,
                                timeDeal.dealPrice,
                                Expressions.constant(TimeDealStatus.OPEN),
                                timeDeal.startAt,
                                timeDeal.endAt
                        ))
                        .from(timeDeal)
                        .join(timeDeal.product)
                        .where(
                                isOpen(now),
                                timeDeal.isDeleted.isFalse()
                        )
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        Long count = queryFactory
                .select(timeDeal.count())
                .from(timeDeal)
                .where(
                        isOpen(now),
                        timeDeal.isDeleted.isFalse()
                )
                .fetchOne();

        return new PageImpl<>(list, pageable, count == null ? 0 : count);
    }

    private BooleanExpression isOpen(LocalDateTime now) {
        return timeDeal.startAt.loe(now).and(timeDeal.endAt.goe(now));
    }


    @Override
    public boolean existsActiveDealByProduct(Long productId, LocalDateTime now) {
        return queryFactory.selectFrom(timeDeal)
                .where(
                        timeDeal.product.id.eq(productId),
                        timeDeal.isDeleted.isFalse(),
                        timeDeal.startAt.loe(now),
                        timeDeal.endAt.goe(now)
                )
                .fetchFirst() != null;
    }

}
