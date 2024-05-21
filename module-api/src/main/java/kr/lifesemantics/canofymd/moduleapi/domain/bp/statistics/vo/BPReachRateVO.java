package kr.lifesemantics.canofymd.moduleapi.domain.bp.statistics.vo;

import lombok.*;

/**
 * @packageName    : kr.lifesemantics.canofymd.moduleapi.domain.statistics.vo
 * @fileName       : BPReachRateVO
 * @author         : ms.jo
 * @date           : 2024/04/30
 * @description    : 혈압 달성률
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/04/30        ms.jo       최초 생성
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode
public class BPReachRateVO {

    private double  dangerousRate;
    private double warningRate;
    private BPSummaryVO goalRate;


}
