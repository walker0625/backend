package kr.lifesemantics.canofymd.modulecore.domain.bp;

import jakarta.persistence.*;
import kr.lifesemantics.canofymd.modulecore.domain.base.BaseTime;
import kr.lifesemantics.canofymd.modulecore.domain.bp.value.BloodPressure;
import kr.lifesemantics.canofymd.modulecore.domain.user.Hospital;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/*@Entity
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(exclude = "patient")
@Comment("혈압 기록")*/
public class BPPatientStatistics extends BaseTime {

    /*@Id
    @Column(name = "bp_summary_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("혈압 기록 요약 순차번호")
    Long seq;

    @Comment("측정 횟수")
    int measureCount;

    @Comment("해당 주차")
    int week;

    // TODO created_at으로 대체하는 것 고려
    @Comment("요약 기준일")
    LocalDate summaryDate;

//    // TODO 위험, 주의 등의 상태를 담은 ENUM으로 기획 확정 후 교체
//    //@Enumerated(EnumType.STRING)
//    @Comment("혈압 상태")
//    String status;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "systolic", column = @Column(name = "systolic")),
            @AttributeOverride(name = "diastolic", column = @Column(name = "diastolic")),
            @AttributeOverride(name = "pulse", column = @Column(name = "pulse"))
    })
    BloodPressure bloodPressure;

    @Comment("수축기 표준 편차")
    double systolicSd;

    @Comment("이완기 표준 편차")
    double diastolicSd;

    @Comment("수축기 변동계수")
    double systolicRsd;

    @Comment("이완기 변동계수")
    double diastolicRsd;

    @Comment("수축기 목표혈압 도달률(%)")
    int systolicReachGoalRate;

    @Comment("이완기 목표혈압 도달률(%)")
    int diastolicReachGoalRate;

    @Comment("주의 혈압 도달률(%)")
    @Column(name = "warning_bp_rate")
    int warningBPRate;

    @Comment("위험 혈압 도달률(%)")
    @Column(name = "dangerous_bp_rate")
    int dangerousBPRate;

    @Comment("알람 횟수")
    int alarmCount;

    @Comment("예측가능여부")
    boolean isPredictable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_seq")
    @Comment("환자")
    Patient patient;

    @OneToMany(mappedBy = "summary", fetch = FetchType.LAZY)
    List<BpHistory> histories;


    protected BPPatientStatistics(int measureCount, int week, LocalDate summaryDate, BloodPressure bloodPressure,
                               double systolicSd, double diastolicSd, double systolicRsd, double diastolicRsd,
                               int systolicReachGoalRate, int diastolicReachGoalRate, int warningBPRate, int dangerousBPRate,
                               int alarmCount, boolean isPredictable, Patient patient) {

        this.measureCount = measureCount;
        this.week = week;
        this.summaryDate = summaryDate;
        this.bloodPressure = bloodPressure;
        this.systolicSd = systolicSd;
        this.diastolicSd = diastolicSd;
        this.systolicRsd = systolicRsd;
        this.diastolicRsd = diastolicRsd;
        this.systolicReachGoalRate = systolicReachGoalRate;
        this.diastolicReachGoalRate = diastolicReachGoalRate;
        this.warningBPRate = warningBPRate;
        this.dangerousBPRate = dangerousBPRate;
        this.alarmCount = alarmCount;
        this.isPredictable = isPredictable;
        this.patient = patient;
    }

    public static BPPatientStatistics create(int measureCount, int week, LocalDate summaryDate, BloodPressure bloodPressure,
                                   double systolicSd, double diastolicSd, double systolicRsd, double diastolicRsd,
                                   int systolicReachGoalRate, int diastolicReachGoalRate, int warningBPRate, int dangerousBPRate,
                                   int alarmCount, boolean isPredictable, Patient patient) {
        return new BPPatientStatistics(measureCount, week, summaryDate, bloodPressure, systolicSd, diastolicSd, systolicRsd, diastolicRsd,
                systolicReachGoalRate, diastolicReachGoalRate, warningBPRate,   dangerousBPRate, alarmCount, isPredictable, patient);
    }*/
}