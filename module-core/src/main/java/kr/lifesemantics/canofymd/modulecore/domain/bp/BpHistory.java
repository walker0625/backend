package kr.lifesemantics.canofymd.modulecore.domain.bp;

import jakarta.persistence.*;
import kr.lifesemantics.canofymd.modulecore.domain.base.BaseTime;
import kr.lifesemantics.canofymd.modulecore.domain.bp.value.BloodPressure;
import kr.lifesemantics.canofymd.modulecore.domain.user.Patient;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(exclude = "patient")
@Comment("혈압 기록")
public class BpHistory extends BaseTime {

    @Id
    @Column(name = "bp_history_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("혈압 기록 순차번호")
    Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_seq")
    @Comment("환자 순차번호")
    Patient patient;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "systolic", column = @Column(name = "first_systolic")),
            @AttributeOverride(name = "diastolic", column = @Column(name = "first_diastolic")),
            @AttributeOverride(name = "pulse", column = @Column(name = "first_pulse")),
            @AttributeOverride(name = "status", column = @Column(name = "first_status"))
    })
    BloodPressure firstBP;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "systolic", column = @Column(name = "second_systolic")),
            @AttributeOverride(name = "diastolic", column = @Column(name = "second_diastolic")),
            @AttributeOverride(name = "pulse", column = @Column(name = "second_pulse")),
            @AttributeOverride(name = "status", column = @Column(name = "second_status"))
    })
    BloodPressure secondBP;

    @Comment("주차")
    int week;

    // index 용도 컬럼
    @Comment("측정일")
    LocalDate measureDate;

    @Comment("측정일시")
    LocalDateTime measureTime;

    @Comment("사용 가능 여부")
    boolean isUsable;

    @Builder
    public BpHistory(Long seq, Patient patient, BloodPressure firstBP, BloodPressure secondBP, int week, LocalDate measureDate, LocalDateTime measureTime, boolean isUsable) {
        this.seq = seq;
        this.patient = patient;
        this.firstBP = firstBP;
        this.secondBP = secondBP;
        this.week = week;
        this.measureDate = measureDate;
        this.measureTime = measureTime;
        this.isUsable = isUsable;
    }

    public static BpHistory create(Patient patient, BloodPressure firstBP, BloodPressure secondBP, int week,
                                   LocalDateTime measureTime, boolean usable) {

        return BpHistory.builder()
                            .patient(patient)
                            .firstBP(firstBP)
                            .secondBP(secondBP)
                            .week(week)
                            .measureDate(measureTime.toLocalDate())
                            .measureTime(measureTime)
                            .isUsable(usable)
                        .build();
    }

    public void modify(BloodPressure firstBP, BloodPressure secondBP, LocalDateTime measureTime, boolean isUsable) {
        this.firstBP = firstBP;
        this.secondBP = secondBP;
        this.measureDate = measureTime.toLocalDate();
        this.measureTime = measureTime;
        this.isUsable = isUsable;
    }

    public boolean isPredictUsable() {
        int hour = this.measureTime.getHour();
        return hour >= 4 && hour < 12;
    }

    public boolean isDanger() {
        return (
                (firstBP.getSystolic() >= 180 && firstBP.getDiastolic() >= 120) 
                && (secondBP.getSystolic() >= 180 && secondBP.getDiastolic() >= 120)
        ) ||
        (
                (firstBP.getSystolic() < 80 && firstBP.getDiastolic() < 50)
                && secondBP.getSystolic() < 80 && secondBP.getDiastolic() < 50
        )
                ;
    }
    
}