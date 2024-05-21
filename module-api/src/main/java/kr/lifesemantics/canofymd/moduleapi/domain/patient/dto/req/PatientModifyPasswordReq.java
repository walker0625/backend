package kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.req
 * fileName       : PatientModifyPasswordReq
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
@Schema(title = "환자 패스워드 변경 DTO")
public class PatientModifyPasswordReq {

    @Schema(description = "비밀번호", example = "qwer1234!@#$")
    String password;

}
