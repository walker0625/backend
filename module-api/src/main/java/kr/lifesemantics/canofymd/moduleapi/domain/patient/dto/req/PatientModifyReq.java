package kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.lifesemantics.canofymd.modulecore.enums.Gender;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.req
 * fileName       : PatientModifyReq
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
public class PatientModifyReq {

    @Schema(description = "환자 이름", example = "김환자")
    String name;

    @Schema(description = "연락처", example = "010-1111-2222")
    String contact;

    @Schema(description = "성별", example = "MALE")
    Gender gender;

    @Schema(description = "생년월일", example = "1995-02-27")
    LocalDate birthDate;

    @Schema(description = "키", example = "177.7")
    double height;

    @Schema(description = "체중", example = "77.7")
    double weight;

    @Schema(description = "음주 여부", example = "true")
    boolean isDrink;

    @Schema(description = "흡연 여부", example = "false")
    boolean isSmoke;

    @Schema(description = "가졸력 여부", example = "false")
    boolean isFamilyDisease;

}
