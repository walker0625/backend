package kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.statistics.vo.BPReachRateVO;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.statistics.vo.BPSummaryVO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.res
 * fileName       : BpStatisticsRes
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
@Schema(title = "주/월 차 별 통계 DTO")
@AllArgsConstructor
public class BpStatisticsRes {

        @Schema(description = "주/월차")
        int standard;

        @Schema(description = "수축기/이완기 평균")
        BPSummaryVO average;

        @Schema(description = "수축기/이완기 표준편차")
        BPSummaryVO standardDeviation;

        @Schema(description = "수축기/이완기 변동 계수")
        BPSummaryVO relativeStandardDeviation;

        @Schema(description = "목표/주의/위험 혈압 도달률")
        BPReachRateVO reachRatePerState;

        @Schema(description = "알람횟수(회)")
        int alarmCount;

}
