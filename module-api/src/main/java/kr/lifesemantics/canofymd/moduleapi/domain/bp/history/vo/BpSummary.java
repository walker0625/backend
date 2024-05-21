package kr.lifesemantics.canofymd.moduleapi.domain.bp.history.vo;

import kr.lifesemantics.canofymd.modulecore.domain.bp.BpHistory;
import kr.lifesemantics.canofymd.modulecore.domain.bp.value.BloodPressure;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EqualsAndHashCode
@ToString
public class BpSummary {

    //해당 주차
    int week;

    //요약 기준일
    LocalDate summaryDate;

    //측정 횟수
    int measureCount; // TODO 한번 측정에 +2 / 삭제시 -2

    //알람 숫자
    int alarmCount;

    BloodPressure bloodPressure;

    List<BpHistory> histories;

    public BpSummary(int week, LocalDate summaryDate, int measureCount, int alarmCount, BloodPressure bloodPressure, List<BpHistory> histories) {
        this.week = week;
        this.summaryDate = summaryDate;
        this.measureCount = measureCount;
        this.alarmCount = alarmCount;
        this.bloodPressure = bloodPressure;
        this.histories = histories;
    }

    public BpSummary(int week, LocalDate date, int size, int dangerCount, int round, int round1, int round2, List<BpHistory> histories) {
        this.week = week;
        this.summaryDate = date;
        this.measureCount = size;
        this.alarmCount = dangerCount;
        this.histories = histories;
        this.bloodPressure = new BloodPressure(round, round1, round2);
    }

}