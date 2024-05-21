package kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.res;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Transient;
import kr.lifesemantics.canofymd.modulecore.enums.Gender;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.res
 * fileName       : PatientParticipationDangerousListRes
 * author         : ms.jo
 * date           : 2024/04/25
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/04/25        ms.jo       최초 생성
 */

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(title = "환자 조회 결과 DTO")
public class PatientParticipationDangerousListRes {

    @Schema(description = "환자 순차번호", example = "1")
    Long seq;

    @Schema(description = "환자 이름", example = "김환자")
    String name;

    @Schema(description = "생년월일", example = "1966-12-11")
    LocalDate birthDate;

    @Schema(description = "연락처", example = "010-5511-2238")
    String contact;

    @Schema(description = "성별", example = "FEMALE")
    Gender gender;

    @Schema(description = "마지막 측정일자", example = "2024-04-23")
    LocalDate lastParticipationDate;

    @Transient
    String pushToken;

    @Builder
    @QueryProjection
    public PatientParticipationDangerousListRes(Long seq, String name, LocalDate birthDate, String contact, Gender gender, LocalDate lastParticipationDate, String pushToken) {
        this.seq = seq;
        this.name = name;
        this.birthDate = birthDate;
        this.contact = contact;
        this.gender = gender;
        this.lastParticipationDate = lastParticipationDate;
        this.pushToken = pushToken;
    }

}
