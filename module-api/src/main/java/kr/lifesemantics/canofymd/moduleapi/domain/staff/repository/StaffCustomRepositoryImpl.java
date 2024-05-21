package kr.lifesemantics.canofymd.moduleapi.domain.staff.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.lifesemantics.canofymd.moduleapi.domain.staff.dto.res.QStaffListRes;
import kr.lifesemantics.canofymd.moduleapi.domain.staff.dto.res.StaffListRes;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static kr.lifesemantics.canofymd.modulecore.domain.user.QStaff.staff;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StaffCustomRepositoryImpl implements StaffCustomRepository {

    JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<StaffListRes> findStaffsByConditionWithPaging(Long hospitalSeq, String keyword, Pageable pageable) {

        QueryResults<StaffListRes> results = jpaQueryFactory
            .select(new QStaffListRes(
                staff.seq,
                staff.id,
                staff.name,
                staff.userType,
                staff.contact,
                staff.createdAt,
                staff.isActive
            ))
            .from(staff)
            .where(
                staff.hospital.seq.eq(hospitalSeq),
                idContainsIgnoreCase(keyword).or(nameContainsIgnoreCase(keyword))
            )
            .orderBy(staff.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetchResults();

        List<StaffListRes> contents = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(contents, pageable, total);
    }

    private BooleanExpression idContainsIgnoreCase(String keyword) {
        return hasText(keyword) ? staff.id.containsIgnoreCase(keyword) : Expressions.TRUE;
    }

    private BooleanExpression nameContainsIgnoreCase(String keyword) {
        return hasText(keyword) ? staff.name.containsIgnoreCase(keyword) : Expressions.TRUE;
    }

}