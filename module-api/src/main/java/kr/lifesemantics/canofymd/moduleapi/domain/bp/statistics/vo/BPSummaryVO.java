package kr.lifesemantics.canofymd.moduleapi.domain.bp.statistics.vo;

import lombok.*;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.statistics.vo
 * fileName       : BPSummaryVO
 * author         : ms.jo
 * date           : 2024/04/30
 * description    :
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
public class BPSummaryVO {

    private double systolicSummary;
    private double diastolicSummary;

}
