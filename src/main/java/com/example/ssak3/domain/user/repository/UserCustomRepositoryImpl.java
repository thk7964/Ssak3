package com.example.ssak3.domain.user.repository;

import com.example.ssak3.common.enums.UserRole;
import com.example.ssak3.domain.admin.model.response.UserListGetResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static com.example.ssak3.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<UserListGetResponse> getUserList(UserRole role, String nickname, Pageable pageable) {

        Long total = queryFactory
                .select(user.countDistinct())
                .from(user)
                .where(
                        roleEq(role),
                        nicknameContains(nickname),
                        user.isDeleted.eq(false)
                )
                .fetchOne();

        long totalCount = (total != null) ? total : 0L;

        return new PageImpl<>(
                queryFactory
                        .select(Projections.constructor(UserListGetResponse.class,
                                user.id,
                                user.name,
                                user.nickname,
                                user.email,
                                user.phone,
                                user.createdAt,
                                user.updatedAt))
                        .from(user)
                        .where(
                                roleEq(role),
                                nicknameContains(nickname),
                                user.isDeleted.eq(false)
                        )
                        .orderBy(user.nickname.asc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch(),
                pageable,
                totalCount);
    }

    private BooleanExpression roleEq(UserRole role) {
        return (role != null) ? user.role.eq(role) : null;
    }

    private BooleanExpression nicknameContains(String nickname) {
        return (nickname != null && !nickname.isBlank()) ? user.nickname.contains(nickname) : null;
    }

}