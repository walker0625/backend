package kr.lifesemantics.canofymd.moduleapi.domain.hospital.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.lifesemantics.canofymd.moduleapi.domain.hospital.dto.res.HospitalFindListRes;
import kr.lifesemantics.canofymd.moduleapi.domain.hospital.dto.res.QHospitalFindListRes;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static kr.lifesemantics.canofymd.modulecore.domain.user.QHospital.hospital;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HospitalCustomRepositoryImpl implements HospitalCustomRepository {

    JPAQueryFactory queryFactory;

    // TODO 추후에도 동적 조건 필요없으면 쿼리메소드로 간소화
    @Override
    public Page<HospitalFindListRes> findHospitalsByName(String keyword, Pageable pageable) {

        QueryResults<HospitalFindListRes> results;

        results = queryFactory
                .select(
                        new QHospitalFindListRes(
                                hospital.seq,
                                hospital.name,
                                hospital.managerName,
                                hospital.managerContact,
                                hospital.address,
                                hospital.createdAt,
                                hospital.isActive
                        )
                )
                .from(hospital)
                .where(
                        nameContainsIgnoreCase(keyword)
                )
                .orderBy(hospital.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<HospitalFindListRes> findListResList = results.getResults();

        long total = results.getTotal();

        return new PageImpl<>(findListResList, pageable, total);
    }

    private BooleanExpression nameContainsIgnoreCase(String keyword) {
        return hasText(keyword) ? hospital.name.containsIgnoreCase(keyword) : Expressions.TRUE;
    }

}