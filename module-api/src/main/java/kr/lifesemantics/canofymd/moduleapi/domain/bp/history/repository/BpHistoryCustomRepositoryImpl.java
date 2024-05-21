package kr.lifesemantics.canofymd.moduleapi.domain.bp.history.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BpHistoryCustomRepositoryImpl implements BpHistoryCustomRepository {

    JPAQueryFactory queryFactory;

}
