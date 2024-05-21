package kr.lifesemantics.canofymd.moduleapi.domain.dashboard.dto.res;

import kr.lifesemantics.canofymd.moduleapi.domain.hospital.enums.TransitionDirection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardStatisticsRes {

    int totalPatient;
    TransitionDirection totalPatientDirection;

    int yesterdayMeasurePatient;
    TransitionDirection yesterdayMeasureDirection;

    int todayMeasurePatient;
    TransitionDirection todayMeasureDirection;

    int accumulatePredicationPatient;
    TransitionDirection accumulatePredicationDirection;

}
