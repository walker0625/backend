package kr.lifesemantics.canofymd.moduleapi.domain.patient.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.controller.PatientFindByDoctorListRes;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.controller.QPatientFindByDoctorListRes;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.res.PatientFindListRes;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.res.PatientParticipationDangerousListRes;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.res.QPatientFindListRes;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.res.QPatientParticipationDangerousListRes;
import kr.lifesemantics.canofymd.modulecore.domain.user.Patient;
import kr.lifesemantics.canofymd.modulecore.enums.Category;
import kr.lifesemantics.canofymd.modulecore.enums.ParticipationCompliance;
import kr.lifesemantics.canofymd.modulecore.enums.UserType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.QuerydslUtils;

import java.util.List;


import static kr.lifesemantics.canofymd.modulecore.domain.user.QHospital.hospital;
import static kr.lifesemantics.canofymd.modulecore.domain.user.QPatient.patient;
import static kr.lifesemantics.canofymd.modulecore.domain.user.QPatientCategory.patientCategory;
import static org.springframework.util.StringUtils.hasText;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.patient.repository
 * fileName       : PatientCustomRepositoryImpl
 * author         : ms.jo
 * date           : 2024/04/25
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/04/25        ms.jo       최초 생성
 */

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientCustomRepositoryImpl implements PatientCustomRepository {

    JPAQueryFactory queryFactory;

    @Override
    public Page<PatientFindListRes> findPatientsByFilterWithPaging(UserType userType, Category category, Long hospitalSeq,
                                                                   List<ParticipationCompliance> filters, String keyword, Pageable pageable) {

        QueryResults<PatientFindListRes> result;

        result = queryFactory.select(
            new QPatientFindListRes(
                    patient.seq,
                    patient.id,
                    patient.name,
                    patient.birthDate,
                    patient.contact,
                    patient.gender,
                    patient.createdAt,
                    patient.status
            )
        )
        .from(patientCategory)
        .innerJoin(patientCategory.patient, patient)
        .innerJoin(patient.hospital, hospital)
        .where(
            nameContainsIgnoreCase(keyword),
            isNotAdminOnlyCanSearchBelongHospital(userType, hospitalSeq),
            containsStatus(filters),
            attachedCategory(category)
        )
        .orderBy(patient.createdAt.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetchResults();

        List<PatientFindListRes> patients = result.getResults();

        long total = result.getTotal();

        return new PageImpl<>(patients, pageable, total);
    }

    @Override
    public Page<PatientFindByDoctorListRes> findPatientsByDoctorPaging(Category category, Long doctorSeq, String keyword, Pageable pageable) {
        QueryResults<PatientFindByDoctorListRes> result;

        result = queryFactory.select(
            new QPatientFindByDoctorListRes(
                    patient.seq,
                    patient.name,
                    patient.gender,
                    patient.birthDate
            )
        )
        .from(patientCategory)
        .innerJoin(patientCategory.patient, patient)
        .where(
            nameContainsIgnoreCase(keyword),
            isManagedByDoctor(doctorSeq),
            attachedCategory(category)
        )
        .orderBy(patient.createdAt.desc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetchResults();

        List<PatientFindByDoctorListRes> patients = result.getResults();

        long total = result.getTotal();

        return new PageImpl<>(patients, pageable, total);

    }

    @Override
    public List<PatientParticipationDangerousListRes> findByHospitalSeqAndStatus(UserType userType, Long hospitalSeq, ParticipationCompliance status, Category category) {

        QueryResults<PatientParticipationDangerousListRes> result;

        result = queryFactory.select(
            new QPatientParticipationDangerousListRes(
                patient.seq,
                patient.name,
                patient.birthDate,
                patient.contact,
                patient.gender,
                patient.lastMeasuredDate,
                patient.pushToken
            )
        )
        .from(patientCategory)
        .innerJoin(patientCategory.patient, patient)
        .innerJoin(patient.hospital, hospital)
        .where(
            attachedCategory(category),
            patient.status.eq(status),
            isNotAdminOnlyCanSearchBelongHospital(userType, hospitalSeq)
        )
        .fetchResults();

        return result.getResults();
    }

    private BooleanExpression nameContainsIgnoreCase(String keyword) {
        return hasText(keyword) ? patient.name.containsIgnoreCase(keyword) : Expressions.TRUE;
    }

    private BooleanExpression isNotAdminOnlyCanSearchBelongHospital(UserType userType, Long hospitalSeq) {
        return userType != UserType.ADMIN ? hospital.seq.eq(hospitalSeq) : Expressions.TRUE;
    }

    private BooleanExpression containsStatus(List<ParticipationCompliance> filters) {
        return filters.isEmpty() ? Expressions.TRUE : patient.status.in(filters);
    }

    private BooleanExpression attachedCategory(Category category) {
        return patientCategory.category.eq(category);
    }

    private BooleanExpression isManagedByDoctor(Long doctorSeq) {
        return patient.staff.seq.eq(doctorSeq);
    }

}
