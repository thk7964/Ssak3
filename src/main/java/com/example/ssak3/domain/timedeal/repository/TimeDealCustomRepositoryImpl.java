package com.example.ssak3.domain.timedeal.repository;

import com.example.ssak3.common.enums.TimeDealStatus;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
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
    public Page<TimeDealListGetResponse> findTimeDeals(TimeDealStatus status, Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        List<TimeDeal> timeDeals =
                queryFactory.selectFrom(timeDeal)
                        .join(timeDeal.product).fetchJoin()
                        .where(
                                timeDealStatusCondition(status, now),
                                timeDeal.isDeleted.isFalse()
                        )
                        .orderBy(timeDeal.endAt.asc())// 종료 임박순
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        List<TimeDealListGetResponse>list =timeDeals.stream()
                .map(TimeDealListGetResponse::from)
                .toList();

        Long count = queryFactory
                .select(timeDeal.count())
                .from(timeDeal)
                .where(
                        timeDealStatusCondition(status, now),
                        timeDeal.isDeleted.isFalse()
                )
                .fetchOne();

        return new PageImpl<>(list, pageable, count == null ? 0 : count);
    }

    private BooleanExpression timeDealStatusCondition(TimeDealStatus status, LocalDateTime now) {

        if (status == null){
            return null;
        }

        return switch (status) {
            case READY -> timeDeal.startAt.gt(now);
            case OPEN -> timeDeal.startAt.loe(now)
                    .and(timeDeal.endAt.goe(now));
            case CLOSED -> timeDeal.endAt.lt(now);
            default -> null;
        };

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
