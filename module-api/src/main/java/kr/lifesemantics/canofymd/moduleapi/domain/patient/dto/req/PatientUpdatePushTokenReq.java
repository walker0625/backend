package kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.req
 * fileName       : PatientUpdatePushTokenReq
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
@Schema(title = " DTO")
@AllArgsConstructor
public class PatientUpdatePushTokenReq {

    @Schema(description = "푸시토큰", example = "qwehfdsiufhsdkfjhdskfhsdfdshf213u21swjhfksj893")
    String token;

}
