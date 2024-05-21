package kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.lifesemantics.canofymd.modulecore.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.res
 * fileName       : PatientPersonalRes
 * author         : ms.jo
 * date           : 2024/05/02
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/05/02        ms.jo       최초 생성
 */
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(title = "환자 인적사항 조회 결과 DTO")
@AllArgsConstructor
@Builder
public class PatientPersonalRes {

    @Schema(description = "논리 ID", example = "patient1")
    String id;
    @Schema(description = "이름", example = "김건강")
    String name;

    @Schema(description = "연락처", example = "010-1111-2222")
    String contact;
    @Schema(description = "성별", example = "MALE")
    Gender gender;
    @Schema(description = "생년월일", example = "1995-02-27")
    LocalDate birthDate;
    @Schema(description = "등록일", example = "2024-01-01")
    LocalDate createdDate;
    @Schema(description = "키(cm)", example = "177")
    double height;
    @Schema(description = "체중(kg)", example = "68")
    double weight;
    @Schema(description = "음주여부", example = "false")
    boolean isDrink;
    @Schema(description = "흡연여부", example = "false")
    boolean isSmoke;

}
