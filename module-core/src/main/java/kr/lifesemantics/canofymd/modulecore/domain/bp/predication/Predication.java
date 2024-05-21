package kr.lifesemantics.canofymd.modulecore.domain.bp.predication;

import jakarta.persistence.*;
import kr.lifesemantics.canofymd.modulecore.domain.base.BaseTime;
import kr.lifesemantics.canofymd.modulecore.domain.user.Patient;
import kr.lifesemantics.canofymd.modulecore.domain.bp.value.SystolicDiastolic;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

/**
 * packageName    : kr.lifesemantics.canofymd.modulecore.domain.bp.predication
 * fileName       : Predication
 * author         : ms.jo
 * date           : 2024/05/03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/05/03        ms.jo       최초 생성
 */
@Entity
@Getter
@DynamicUpdate
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Comment("예측")
public class Predication extends BaseTime {

    @Id
    @Column(name = "predication_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("예측 순차번호")
    Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_seq")
    Patient patient;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "systolic", column = @Column(name = "week1_systolic")),
            @AttributeOverride(name = "diastolic", column = @Column(name = "week1_diastolic"))
    })
    SystolicDiastolic predicatedWeek1;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "systolic", column = @Column(name = "week2_systolic")),
            @AttributeOverride(name = "diastolic", column = @Column(name = "week2_diastolic"))
    })
    SystolicDiastolic predicatedWeek2;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "systolic", column = @Column(name = "week3_systolic")),
            @AttributeOverride(name = "diastolic", column = @Column(name = "week3_diastolic"))
    })
    SystolicDiastolic predicatedWeek3;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "systolic", column = @Column(name = "week4_systolic")),
            @AttributeOverride(name = "diastolic", column = @Column(name = "week4_diastolic"))
    })
    SystolicDiastolic predicatedWeek4;

    protected Predication(Patient patient,
                       SystolicDiastolic predicatedWeek1, SystolicDiastolic predicatedWeek2,
                       SystolicDiastolic predicatedWeek3, SystolicDiastolic predicatedWeek4) {
        this.patient = patient;
        this.predicatedWeek1 = predicatedWeek1;
        this.predicatedWeek2 = predicatedWeek2;
        this.predicatedWeek3 = predicatedWeek3;
        this.predicatedWeek4 = predicatedWeek4;
    }

    public static Predication create(Patient patient,
                           SystolicDiastolic predicatedWeek1, SystolicDiastolic predicatedWeek2,
                           SystolicDiastolic predicatedWeek3, SystolicDiastolic predicatedWeek4) {

        return new Predication(patient, predicatedWeek1, predicatedWeek2, predicatedWeek3, predicatedWeek4);
    }
}
