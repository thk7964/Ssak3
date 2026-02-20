package com.example.ssak3.domain.timedeal.repository;

import com.example.ssak3.common.enums.TimeDealStatus;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import com.example.ssak3.domain.timedeal.model.response.TimeDealListGetResponse;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.ssak3.domain.product.entity.QProduct.product;
import static com.example.ssak3.domain.timedeal.entity.QTimeDeal.timeDeal;

@RequiredArgsConstructor
public class TimeDealCustomRepositoryImpl implements TimeDealCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<TimeDealListGetResponse> findTimeDeals(TimeDealStatus status, Pageable pageable) {

        List<TimeDeal> timeDeals =
                queryFactory.selectFrom(timeDeal)
                        .join(timeDeal.product).fetchJoin()
                        .where(
                                timeDealStatusCondition(status),
                                timeDeal.isDeleted.isFalse()
                        )
                        .orderBy(
                                status != null ? getTimeDealByForStatus(status) : getDefaultOrder()
                        )
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        List<TimeDealListGetResponse> list = timeDeals.stream()
                .map(TimeDealListGetResponse::from)
                .toList();

        Long count = queryFactory
                .select(timeDeal.count())
                .from(timeDeal)
                .where(
                        timeDealStatusCondition(status),
                        timeDeal.isDeleted.isFalse()
                )
                .fetchOne();

        return new PageImpl<>(list, pageable, count == null ? 0 : count);
    }

    private OrderSpecifier<?>[] getDefaultOrder() {

        NumberExpression<Integer> statusOrder =
                new CaseBuilder()
                        .when(timeDeal.status.eq(TimeDealStatus.OPEN)).then(0)
                        .when(timeDeal.status.eq(TimeDealStatus.READY)).then(1)
                        .otherwise(2);

        DateTimeExpression<LocalDateTime> openOrder =
                new CaseBuilder()
                        .when(timeDeal.status.eq(TimeDealStatus.OPEN))
                        .then(timeDeal.endAt)
                        .otherwise((LocalDateTime) null);

        DateTimeExpression<LocalDateTime> readyOrder =
                new CaseBuilder()
                        .when(timeDeal.status.eq(TimeDealStatus.READY))
                        .then(timeDeal.startAt)
                        .otherwise((LocalDateTime) null);

        DateTimeExpression<LocalDateTime> closedOrder =
                new CaseBuilder()
                        .when(timeDeal.status.eq(TimeDealStatus.CLOSED))
                        .then(timeDeal.endAt)
                        .otherwise((LocalDateTime) null);

        return new OrderSpecifier[]{
                statusOrder.asc(),
                openOrder.asc(),
                readyOrder.asc(),
                closedOrder.desc()
        };
    }

    /**
     * 단일 상태 정렬
     */
    private OrderSpecifier<?>[] getTimeDealByForStatus(TimeDealStatus status) {

        return switch (status) {
            case OPEN -> new OrderSpecifier[]{timeDeal.endAt.asc()};         //종료 임박순
            case READY -> new OrderSpecifier[]{timeDeal.startAt.asc()};      //오픈 임박순
            case CLOSED -> new OrderSpecifier[]{timeDeal.endAt.desc()};      //종료 최신순
            default -> null;
        };
    }

    /**
     * 상태별 조회 조건
     */
    private BooleanExpression timeDealStatusCondition(TimeDealStatus status) {

        if (status == null) {
            return null;
        }

        return switch (status) {
            case READY -> timeDeal.status.eq(TimeDealStatus.READY);
            case OPEN -> timeDeal.status.eq(TimeDealStatus.OPEN);
            case CLOSED -> timeDeal.status.eq(TimeDealStatus.CLOSED);
            default -> null;
        };
    }

    @Override
    public boolean existsActiveDealByProduct(Long productId) {

        return queryFactory.selectFrom(timeDeal)
                .where(
                        timeDeal.product.id.eq(productId),
                        timeDeal.isDeleted.isFalse(),
                        timeDeal.status.in(TimeDealStatus.READY, TimeDealStatus.OPEN)
                )
                .fetchFirst() != null;
    }

    @Override
    public List<TimeDeal> findReadyToOpen(LocalDateTime now) {

        return queryFactory
                .selectFrom(timeDeal)
                .join(timeDeal.product, product).fetchJoin()
                .where(
                        timeDeal.status.eq(TimeDealStatus.READY),
                        timeDeal.isDeleted.isFalse(),
                        timeDeal.startAt.loe(now)
                )
                .fetch();
    }

    @Override
    public List<TimeDeal> findOpenToClose(LocalDateTime now) {

        return queryFactory
                .selectFrom(timeDeal)
                .join(timeDeal.product, product).fetchJoin()
                .where(
                        timeDeal.status.eq(TimeDealStatus.OPEN),
                        timeDeal.isDeleted.isFalse(),
                        timeDeal.endAt.loe(now)
                )
                .fetch();
    }
}