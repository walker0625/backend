package kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import kr.lifesemantics.canofymd.modulecore.enums.Gender;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.req
 * fileName       : PatientRegisterReq
 * author         : ms.jo
 * date           : 2024/04/25
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/04/25        ms.jo       최초 생성
 */

@Schema(title = "환자 등록 요청 DTO")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class PatientRegisterReq {

    @NotBlank(message = "필수 항목입니다.")
    @Schema(description = "환자 아이디", example = "patient100")
    String id;
    @Schema(description = "환자 이름", example = "김환자")
    String name;
    @Schema(description = "성별", example = "MALE")
    Gender gender;
    @Schema(description = "생년월일", example = "1995-02-27")
    LocalDate birthDate;
    @Schema(description = "연락처", example = "010-1111-2222")
    String contact;
    @Schema(description = "키(cm) 소숫점 한자리", example = "177.7")
    double height;
    @Schema(description = "몸무게(cm) 소숫점 한자리", example = "77.7")
    double weight;
    @Schema(description = "흡연 여부", type = "Boolean", allowableValues = {"true", "false"}, example = "true")
    boolean isSmoke;
    @Schema(description = "음주 여부", type = "Boolean", allowableValues = {"true", "false"}, example = "true")
    boolean isDrink;
    @Schema(description = "가족력 여부", type = "Boolean", allowableValues = {"true", "false"}, example = "true")
    boolean isFamilyDisease;

}
