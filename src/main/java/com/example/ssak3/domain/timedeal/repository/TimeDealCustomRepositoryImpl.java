package com.example.ssak3.domain.timedeal.repository;

import com.example.ssak3.common.enums.TimeDealStatus;
import com.example.ssak3.domain.timedeal.entity.TimeDeal;
import com.example.ssak3.domain.timedeal.model.response.TimeDealListGetResponse;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
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
        LocalDateTime now = LocalDateTime.now();
        List<TimeDeal> timeDeals =
                queryFactory.selectFrom(timeDeal)
                        .join(timeDeal.product).fetchJoin()
                        .where(
                                timeDealStatusCondition(status, now),
                                timeDeal.isDeleted.isFalse()
                        )
                        .orderBy(//status값이 있으면 상태별 정렬 없으면 기본값 정렬
                                status != null ? getTimeDealByForStatus(status) : new OrderSpecifier[]{statusPriority(now)}
                        )
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();
        List<TimeDealListGetResponse> list = timeDeals.stream()
                .map(TimeDealListGetResponse::from)
                .sorted((a,b)->{
                    if (a.getStatus()==TimeDealStatus.OPEN && b.getStatus()==TimeDealStatus.OPEN){
                        return a.getEndAt().compareTo(b.getEndAt());//종료 임박 순
                    }
                    if (a.getStatus()==TimeDealStatus.READY && b.getStatus()==TimeDealStatus.READY){
                        return a.getStartAt().compareTo(b.getStartAt());//오픈 임박 순
                    }
                    if (a.getStatus()==TimeDealStatus.CLOSED && b.getStatus()==TimeDealStatus.CLOSED){
                        return b.getEndAt().compareTo(a.getEndAt()); // 종료 최신 순
                    }
                    return 0;
                })
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

    /**
     * 단일 상태 정렬
     *
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
     * 상태 우선순위
     *
     */
    private OrderSpecifier<?> statusPriority(LocalDateTime now) {
        return new CaseBuilder()
                .when(timeDeal.startAt.loe(now).and(timeDeal.endAt.goe(now))).then(0) //OPEN
                .when(timeDeal.startAt.gt(now)).then(1)//READY
                .otherwise(2)//CLOSED
                .asc();
    }

    /**
     * 상태별 조회 조건
     *
     */
    private BooleanExpression timeDealStatusCondition(TimeDealStatus status, LocalDateTime now) {

        if (status == null) {
            return null;
        }

        return switch (status) {
            case READY -> timeDeal.startAt.gt(now);
            case OPEN -> timeDeal.startAt.loe(now).and(timeDeal.endAt.goe(now));
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
