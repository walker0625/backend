package kr.lifesemantics.canofymd.moduleapi.webClient.dto.res;

import kr.lifesemantics.canofymd.moduleapi.webClient.dto.common.ResponseStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.webClient.dto.res
 * fileName       : PredictionRes
 * author         : ms.jo
 * date           : 2024/05/03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/05/03        ms.jo       최초 생성
 */
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PredictionRes {

    ResponseStatus responseStatus;

    PredictionAmount entity;

    public static PredictionRes createEmptyObject() {
        return new PredictionRes();
    }

}
