package kr.lifesemantics.canofymd.moduleapi.webClient.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.lifesemantics.canofymd.moduleapi.webClient.dto.common.ResponseStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.webClient.dto.res
 * fileName       : PredictionCheckRes
 * author         : ms.jo
 * date           : 2024/05/13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/05/13        ms.jo       최초 생성
 */
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(title = " DTO")
@AllArgsConstructor
public class PredictionCheckRes {

    ResponseStatus responseStatus;
    Boolean result;

}
