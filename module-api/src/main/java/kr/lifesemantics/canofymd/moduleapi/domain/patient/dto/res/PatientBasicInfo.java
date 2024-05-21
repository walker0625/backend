package kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.lifesemantics.canofymd.modulecore.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.res
 * fileName       : PatientBasicInfo
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
@AllArgsConstructor
@Builder
@Schema(title = "환자 조회 결과 DTO")
public class PatientBasicInfo {


    @Schema(description = "환자 순차번호", example = "1")
    Long seq;
    @Schema(description = "성별", example = "MALE")
    Gender gender;
    @Schema(description = "생년월일", example = "2024-05-20")
    LocalDate birthDate;
    @Schema(description = "나이", example = "65")
    int age;
    @Schema(description = "BMI", example = "20.00")
    double bmi;
    @Schema(description = "흡연 여부", example = "false")
    boolean isSmoke;
    @Schema(description = "연락처", example = "010-5137-1147")
    String contact;


}
