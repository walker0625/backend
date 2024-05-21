package kr.lifesemantics.canofymd.moduleapi.domain.patient.controller;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.lifesemantics.canofymd.modulecore.enums.Gender;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.patient.controller
 * fileName       : PatientFindByDoctorListRes
 * author         : ms.jo
 * date           : 2024/05/14
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/05/14        ms.jo       최초 생성
 */
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(title = "의료진 별 환자 검색 리스트 DTO")
public class PatientFindByDoctorListRes {

    Long seq;
    String name;
    Gender gender;
    LocalDate birthDate;

    @QueryProjection
    public PatientFindByDoctorListRes(Long seq, String name, Gender gender, LocalDate birthDate) {
        this.seq = seq;
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
    }
}
