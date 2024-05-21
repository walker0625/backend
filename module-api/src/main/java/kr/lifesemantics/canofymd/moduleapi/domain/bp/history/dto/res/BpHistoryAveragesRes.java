package kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Comment;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.res
 * fileName       : BpHistoryAveragesRes
 * author         : ms.jo
 * date           : 2024/05/20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/05/20        ms.jo       최초 생성
 */
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(title = "기간 별 요소 평균 값 DTO")
@AllArgsConstructor
public class BpHistoryAveragesRes {

    @Schema(description = "")
    int systolic;
    int diastolic;
    int pulse;
    int pulsePressure;
    int arterialPressure;

}
