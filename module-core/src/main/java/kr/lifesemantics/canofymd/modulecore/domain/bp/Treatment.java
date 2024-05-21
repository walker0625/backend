package kr.lifesemantics.canofymd.modulecore.domain.bp;

import jakarta.persistence.*;
import kr.lifesemantics.canofymd.modulecore.domain.base.BaseTime;
import kr.lifesemantics.canofymd.modulecore.domain.user.Patient;
import kr.lifesemantics.canofymd.modulecore.enums.BPControl;
import kr.lifesemantics.canofymd.modulecore.enums.CardiovascularDiseaseRisk;
import kr.lifesemantics.canofymd.modulecore.enums.MedicationControl;
import kr.lifesemantics.canofymd.modulecore.enums.ProtLevel;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Entity
@Getter
@DynamicUpdate
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "patient")
@Comment("진료")
public class Treatment extends BaseTime {

    @Id
    @Column(name = "treatment_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("진료 순차번호")
    Long seq;

    @ManyToOne
    @JoinColumn(name = "patient_seq")
    @Comment("환자")
    Patient patient;

    @Comment("심뇌혈관질환 위험도")
    @Enumerated(EnumType.STRING)
    CardiovascularDiseaseRisk cardiovascularDiseaseRisk = null;

    @Comment("목표 수축기 혈압(-1 시 판단불가)")
    int goalSystolic = -1;

    @Comment("목표 이완기 혈압(-1 시 판단불가)")
    int goalDiastolic = -1;

    @Comment("목표 LDL 콜레스테롤 수치(-1 시 판단불가)")
    int goalLDL = -1;

    @Comment("혈압 조절")
    @Enumerated(EnumType.STRING)
    BPControl bpControl = null;

    @Comment("약제 조절")
    @Enumerated(EnumType.STRING)
    MedicationControl medicationControl = null;

    @Comment("진료일")
    LocalDate treatedAt;

    boolean hasCAD;
    boolean hasPAOD;
    boolean hasCVA;
    boolean hasCKD;
    boolean hasHF;
    boolean hasAneurysm;
    boolean hasHypertension;
    boolean hasHL;
    boolean hasDM;

    @Comment("측정 검사 여부")
    boolean hasMeasurement;
    @Comment("측정 검사일")
    LocalDate measuredDate;
    Integer systolic;
    Integer diastolic;

    @Comment("혈액 검사 여부")
    boolean hasBloodCheck;
    @Comment("혈액 검사일")
    LocalDate bloodCheckDate;
    Integer tChol;
    Integer tg;
    Integer hdl;
    Integer ldl;
    Integer fbs;

    @Comment("소변 검사 여부")
    boolean hasUrineTest;
    @Comment("소변 검사일")
    LocalDate urineTestDate;
    ProtLevel uProt;
    Integer uAlbDCr;


    @Comment("심전도 검사 여부")
    boolean hasElectrocardiogramTest;
    @Comment("심전도 검사일")
    LocalDate electrocardiogramTestDate;
    Boolean lvh;


    private Treatment(Patient patient,
                     boolean hasCAD, boolean hasPAOD, boolean hasCVA, boolean hasCKD, boolean hasHF,
                     boolean hasAneurysm, boolean hasHypertension, boolean hasHL, boolean hasDM,
                     boolean hasMeasurement, LocalDate measuredDate, Integer systolic, Integer diastolic,
                     boolean hasBloodCheck, LocalDate bloodCheckDate, Integer tChol, Integer tg, Integer hdl, Integer ldl, Integer fbs,
                     boolean hasUrineTest, LocalDate urineTestDate, ProtLevel uProt, Integer uAlbDCr,
                     boolean hasElectrocardiogramTest, LocalDate electrocardiogramTestDate, Boolean lvh, LocalDate treatedAt) {

        this.patient = patient;
        this.hasCAD = hasCAD;
        this.hasPAOD = hasPAOD;
        this.hasCVA = hasCVA;
        this.hasCKD = hasCKD;
        this.hasHF = hasHF;
        this.hasAneurysm = hasAneurysm;
        this.hasHypertension = hasHypertension;
        this.hasHL = hasHL;
        this.hasDM = hasDM;
        this.hasMeasurement = hasMeasurement;
        this.measuredDate = measuredDate;
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.hasBloodCheck = hasBloodCheck;
        this.bloodCheckDate = bloodCheckDate;
        this.tChol = tChol;
        this.tg = tg;
        this.hdl = hdl;
        this.ldl = ldl;
        this.fbs = fbs;
        this.hasUrineTest = hasUrineTest;
        this.urineTestDate = urineTestDate;
        this.uProt = uProt;
        this.uAlbDCr = uAlbDCr;
        this.hasElectrocardiogramTest = hasElectrocardiogramTest;
        this.electrocardiogramTestDate = electrocardiogramTestDate;
        this.lvh = lvh;
        this.treatedAt = treatedAt;
    }

    public static Treatment createEmptyObject() {
        return new Treatment();
    }

    public static Treatment create(Patient patient,
                                   boolean hasCAD, boolean hasPAOD, boolean hasCVA, boolean hasCKD, boolean hasHF,
                                   boolean hasAneurysm, boolean hasHypertension, boolean hasHL, boolean hasDM,
                                   boolean hasMeasurement, LocalDate measuredDate, Integer systolic, Integer diastolic,
                                   boolean hasBloodCheck, LocalDate bloodCheckDate, Integer tChol, Integer tg, Integer hdl, Integer ldl, Integer fbs,
                                   boolean hasUrineTest, LocalDate urineTestDate, ProtLevel uProt, Integer uAlbDCr,
                                   boolean hasElectrocardiogramTest, LocalDate electrocardiogramTestDate, Boolean lvh,
                                   LocalDate treatedAt) {

        return new Treatment(
                patient,
                hasCAD, hasPAOD, hasCVA, hasCKD, hasHF, hasAneurysm, hasHypertension, hasHL, hasDM,
                hasMeasurement, measuredDate, systolic, diastolic,
                hasBloodCheck, bloodCheckDate, tChol, tg, hdl, ldl, fbs,
                hasUrineTest, urineTestDate, uProt, uAlbDCr, hasElectrocardiogramTest, electrocardiogramTestDate, lvh, treatedAt
        );
    }

    public void modify(boolean hasCAD, boolean hasPAOD, boolean hasCVA, boolean hasCKD, boolean hasHF,
                             boolean hasAneurysm, boolean hasHypertension, boolean hasHL, boolean hasDM,
                             boolean hasMeasurement, LocalDate measuredDate, Integer systolic, Integer diastolic,
                             boolean hasBloodCheck, LocalDate bloodCheckDate, Integer tChol, Integer tg, Integer hdl, Integer ldl, Integer fbs,
                             boolean hasUrineTest, LocalDate urineTestDate, ProtLevel uProt, Integer uAlbDCr,
                             boolean hasElectrocardiogramTest, LocalDate electrocardiogramTestDate, boolean lvh, LocalDate treatedAt) {

                this.hasCAD = hasCAD; this.hasPAOD = hasPAOD; this.hasCVA = hasCVA; this.hasCKD = hasCKD; this.hasHF = hasHF;
                this.hasAneurysm = hasAneurysm; this.hasHypertension = hasHypertension; this.hasHL = hasHL; this.hasDM = hasDM;
                this.hasMeasurement = hasMeasurement; this.measuredDate = measuredDate; this.systolic = systolic; this.diastolic = diastolic;
                this.hasBloodCheck = hasBloodCheck; this.bloodCheckDate = bloodCheckDate; this.tChol = tChol; this.tg = tg; this.hdl = hdl; this.ldl = ldl; this.fbs = fbs;
                this.hasUrineTest = hasUrineTest; this.urineTestDate = urineTestDate; this.uProt = uProt; this.uAlbDCr = uAlbDCr;
                this.hasElectrocardiogramTest = hasElectrocardiogramTest; this.electrocardiogramTestDate = electrocardiogramTestDate; this.lvh = lvh;
                this.treatedAt = treatedAt;

    }
    
    public void changeCardiovascularDiseaseRisk(CardiovascularDiseaseRisk risk) {
        this.cardiovascularDiseaseRisk = risk;
        this.goalSystolic = risk.getGoalSystolic();
        this.goalDiastolic = risk.getGoalDiastolic();
    }

    public void changeGoalLDL(int ldl) {
        this.goalLDL = ldl;
    }

    public void changeControlItems(BPControl bpControl, MedicationControl medicationControl) {
        this.bpControl = bpControl;
        this.medicationControl = medicationControl;
    }

}
