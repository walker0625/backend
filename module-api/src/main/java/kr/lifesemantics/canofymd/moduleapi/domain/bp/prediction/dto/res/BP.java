package kr.lifesemantics.canofymd.moduleapi.domain.bp.prediction.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.bp.prediction.dto.res
 * fileName       : BP
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
@Schema(title = "혈압 정보 DTO")
@AllArgsConstructor
public class BP {

    @Schema(name = "수축기")
    double systolic;
    @Schema(name = "이완기")
    double diastolic;

}
