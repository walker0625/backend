package kr.lifesemantics.canofymd.moduleapi.domain.bp.summary.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.bp.summary.dto.res
 * fileName       : BpPredictableRes
 * author         : ms.jo
 * date           : 2024/05/08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/05/08        ms.jo       최초 생성
 */
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(title = "예측 가능 여부 및 참여 및 측정 순응도 DTO")
@AllArgsConstructor
public class BpPredictableRes {

    @Schema(description = "예측 가능 여부")
    boolean isPredictable;
    @Schema(description = "참여 순응도")
    double participantRate;
    @Schema(description = "측정 순응도 (최근 7일 중 측정한 날 수)")
    int measuredDateCount;

}
