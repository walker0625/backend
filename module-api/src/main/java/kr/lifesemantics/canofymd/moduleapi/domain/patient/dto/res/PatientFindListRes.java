package kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.res;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Transient;
import kr.lifesemantics.canofymd.modulecore.enums.Gender;
import kr.lifesemantics.canofymd.modulecore.enums.ParticipationCompliance;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.res
 * fileName       : PatientFindListRes
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
public class PatientFindListRes {

    @Schema(description = "환자 순차번호", example = "1")
    Long seq;

    @Schema(description = "환자 아이디", example = "patient1")
    String id;

    @Schema(description = "환자 이름", example = "김환자")
    String name;

    @Schema(description = "생년월일", example = "1995-02-27")
    LocalDate birthDate;

    @Schema(description = "연락처", example = "010-1111-2222")
    String contact;

    @Schema(description = "성별", example = "MALE")
    Gender gender;

    @Schema(description = "가입일", example = "2024-04-24")
    LocalDate createdDate;

    @Transient
    @JsonIgnore
    LocalDateTime createdAt;

    @Schema(description = "상태", example = "WARNING")
    ParticipationCompliance status;

    @Builder
    @QueryProjection
    public PatientFindListRes(Long seq, String id, String name, LocalDate birthDate, String contact, Gender gender, LocalDateTime createdAt, ParticipationCompliance status) {
        this.seq = seq;
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.contact = contact;
        this.gender = gender;
        this.createdAt = createdAt;
        this.createdDate = createdAt.toLocalDate();
        this.status = status;
    }
}
