package kr.lifesemantics.canofymd.moduleapi.domain.bp.prediction.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.statistics.vo.BPSummaryVO;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.bp.prediction.dto.res
 * fileName       : PredictionDetailRes
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
@Schema(title = "예측 결과 상세 DTO")
@AllArgsConstructor
public class PredictionDetailRes {

    @Schema(description = "향후 1W")
    BP week1;
    @Schema(description = "향후 2W")
    BP week2;
    @Schema(description = "향후 3W")
    BP week3;
    @Schema(description = "향후 3W")
    BP week4;

}
