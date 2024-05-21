package kr.lifesemantics.canofymd.moduleapi.domain.patient.repository;

import kr.lifesemantics.canofymd.moduleapi.domain.patient.controller.PatientFindByDoctorListRes;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.res.PatientFindListRes;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.res.PatientParticipationDangerousListRes;
import kr.lifesemantics.canofymd.modulecore.enums.Category;
import kr.lifesemantics.canofymd.modulecore.enums.ParticipationCompliance;
import kr.lifesemantics.canofymd.modulecore.enums.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.patient.repository
 * fileName       : PatientCustomRepository
 * author         : ms.jo
 * date           : 2024/04/25
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/04/25        ms.jo       최초 생성
 */

public interface PatientCustomRepository {

    Page<PatientFindListRes> findPatientsByFilterWithPaging(UserType userType, Category category, Long hospitalSeq, List<ParticipationCompliance> filters, String keyword, Pageable pageable);

    Page<PatientFindByDoctorListRes> findPatientsByDoctorPaging(Category category, Long doctorSeq, String keyword, Pageable pageable);

    List<PatientParticipationDangerousListRes> findByHospitalSeqAndStatus(UserType userType, Long hospitalSeq, ParticipationCompliance participationCompliance, Category category);

}
