package kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.res
 * fileName       : BpHistoryReportRes
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
@Schema(title = "가정 혈압 요약 검사지 반환 DTO")
@AllArgsConstructor
public class BpHistoryReportRes {

    BpHistoryAveragesRes times;
    BpHistoryAveragesRes week;
    BpHistoryAveragesRes month;

}
