package kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.enums.Unit;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.statistics.vo.BPReachRateVO;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.statistics.vo.BPSummaryVO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.res
 * fileName       : BpStatisticsPerUnitRes
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
@Schema(title = "주/월 차 별 통계 종합 DTO")
@AllArgsConstructor
public class BpStatisticsPerUnitRes {

    @Schema(description = "조회 기준")
    Unit unit;

    @Schema(description = "노출 아이템")
    List<BpStatisticsRes> statistics;

}
