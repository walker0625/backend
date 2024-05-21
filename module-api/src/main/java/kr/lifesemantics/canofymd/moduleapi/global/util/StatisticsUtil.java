package kr.lifesemantics.canofymd.moduleapi.global.util;

import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.vo.BpSummary;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.statistics.vo.BPReachRateVO;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.statistics.vo.BPSummaryVO;
import kr.lifesemantics.canofymd.moduleapi.domain.hospital.enums.TransitionDirection;
import kr.lifesemantics.canofymd.modulecore.domain.bp.BpHistory;
import kr.lifesemantics.canofymd.modulecore.domain.user.Patient;
import kr.lifesemantics.canofymd.modulecore.domain.bp.Treatment;
import kr.lifesemantics.canofymd.modulecore.domain.bp.value.BloodPressure;
import kr.lifesemantics.canofymd.modulecore.domain.user.Hospital;
import kr.lifesemantics.canofymd.modulecore.enums.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;


// TODO StatisticsService로 합치는 것 고려
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class StatisticsUtil {

    public static BloodPressure judgeBPStatus(BloodPressure bloodPressure) {

        int systolic = bloodPressure.getSystolic();
        int diastolic = bloodPressure.getDiastolic();

        bloodPressure.setStatus(makeStatus(systolic, diastolic));

        return bloodPressure;
    }

    // systolic을 우선하여 상태값 설정
    public static BPStatus makeStatus(int systolic, int diastolic) {

        if (systolic >= 155) {
            return BPStatus.DANGEROUS;
        } else if (systolic >= 135) {
            return BPStatus.WARNING;
        }

        if (diastolic >= 105) {
            return BPStatus.DANGEROUS;
        } else if (diastolic >= 85) {
            return BPStatus.WARNING;
        }

        return BPStatus.STABLE;
    }

    public static BloodPressure calculateAverage(List<BloodPressure> bloodPressures) {

        if (bloodPressures.isEmpty()) {
            return new BloodPressure(0, 0, 0); // 빈 리스트인 경우 0으로 초기화
        }

        int totalSystolic = bloodPressures.stream().mapToInt(BloodPressure::getSystolic).sum();
        int totalDiastolic = bloodPressures.stream().mapToInt(BloodPressure::getDiastolic).sum();
        int totalPulse = bloodPressures.stream().mapToInt(BloodPressure::getPulse).sum();
        int count = bloodPressures.size();

        int averageSystolic = totalSystolic / count;
        int averageDiastolic = totalDiastolic / count;
        int averagePulse = totalPulse / count;

        BloodPressure bloodPressure = new BloodPressure(averageSystolic, averageDiastolic, averagePulse);

        return bloodPressure;
    }

    public static int judgeWeek(LocalDate firstMeasuredDate, LocalDate checkDate) {

        if(firstMeasuredDate == null) {
            return -1;
        }

        // EX : 5/1 ~ 5/7 = 6 + 1
        long days = ChronoUnit.DAYS.between(firstMeasuredDate, checkDate) + 1L;

        // EX : 7/7 = 1, 8/7 = 2
        return (int)Math.floor(days / 7) + 1;
    }

    public static TransitionDirection judgeDirection(int standard, int compare) {

        if (standard > compare) {
            return TransitionDirection.UP;
        } else if(standard < compare) {
            return TransitionDirection.DOWN;
        } else {
            return TransitionDirection.STAY;
        }

    }


    /**
     * @param summary 마지막 측정일
     * @description 측정순응도 계산
     * @return 측정순응도
     */
    public static int getMeasureRate(BpSummary summary) {
        return (int) (7 - Duration.between(summary.getSummaryDate().atStartOfDay(), LocalDate.now().atStartOfDay()).toDays());
    }

    /**
     * @param summaries : 가입한 지 56일이 지난 사람의 경우 오늘 기준 56일치, 56일 안 지났을 경우 현재까지 모두 측정치
     * description 참여순응도 계산
     * @return 침여순응도
     */
    public static double getParticipantRate(List<BpSummary> summaries, int days) {
        log.info("summaries.size() :: {}", summaries.size());
        log.info("days :: {}", days);

        return Math.round(summaries.stream().filter(item -> item.getMeasureCount() >= 2).count() * 1000 / days) / 10;
    }

    /**
     * @param summaries : 가입한 지 56일이 지난 사람의 경우 오늘 기준 56일치, 56일 안 지났을 경우 현재까지 모두 측정치
     * @description 측정 상태 계산
     * @return 참여순응상태
     */
    public static ParticipationCompliance getParticipationCompliance(List<BpSummary> summaries, int days) {
        if(summaries.isEmpty()) {
            return ParticipationCompliance.WAITING;
        }

        int measureRate = getMeasureRate(summaries.get(summaries.size()-1));
        double participantRate = getParticipantRate(summaries, days);

        if(participantRate >= 60) {
            return switch (measureRate) {
                case 7, 6 -> ParticipationCompliance.NORMAL;
                case 5, 4 -> ParticipationCompliance.WARNING;
                default -> ParticipationCompliance.DANGEROUS;
            };
        }
        else {
            return ParticipationCompliance.DANGEROUS;
        }
    }

    /**
     * @param summaries : 혈압 평균을 계산 할 데이터
     * @description : 혈압 평균 계산
     * @return : 수축/이완기 평균 혈압
     */
    public static BPSummaryVO getAverages(List<BpSummary> summaries) {
        double systolicAvg = summaries.stream()
                .mapToInt(summary -> summary.getBloodPressure().getSystolic())
                .average()
                .orElse(0);
        double diastolicAvg = summaries.stream()
                .mapToInt(summary -> summary.getBloodPressure().getDiastolic())
                .average()
                .orElse(0);

        return new BPSummaryVO(systolicAvg, diastolicAvg);
    }

    /**
     * @param summaries : 표준 편차를 계산 할 데이터
     * @description  : 표준 편차 계산
     * @return : 수축/이완기 표준 편차
     */
    public static BPSummaryVO getStandardDeviation(List<BpSummary> summaries) {

        BPSummaryVO averages = getAverages(summaries);

        double systolicDiffAvg = summaries.stream()
                .mapToDouble(summary -> Math.pow(summary.getBloodPressure().getSystolic() - averages.getSystolicSummary(), 2))
                .average()
                .orElse(0);

        double diastolicDiffAvg = summaries.stream()
                .mapToDouble(summary -> Math.pow(summary.getBloodPressure().getDiastolic() - averages.getDiastolicSummary(), 2))
                .average()
                .orElse(0);

        return new BPSummaryVO(Math.sqrt(systolicDiffAvg), Math.sqrt(diastolicDiffAvg));
    }

    /**
     * @param summaries : 변동계수를 계산 할 데이터
     * @description 변동 계수 계산
     * @return : 수축/이완기 변동계수
     */
    public static BPSummaryVO getRelativeStandardDeviation(List<BpSummary> summaries) {

        BPSummaryVO averages = getAverages(summaries);
        BPSummaryVO standardDeviation = getStandardDeviation(summaries);

        return new BPSummaryVO(
                standardDeviation.getSystolicSummary() / averages.getSystolicSummary() * 100,
                standardDeviation.getDiastolicSummary() / averages.getDiastolicSummary() * 100
        );
    }

    /**
     * @param statistics : 각 혈압 도달률 계산 할 데이터
     * @description 각 혈압 도달률 계산
     * @return : 목표/위험/주의 혈압 도달률
     */
    public static BPReachRateVO getReachRatePerState(Treatment treatment, List<BpSummary> statistics) {


        int goalSystolic = treatment == null ? -1 : treatment.getGoalSystolic();
        int goalDiastolic = treatment == null ? -1 : treatment.getGoalDiastolic();

        long totalCount = statistics.stream().mapToInt(BpSummary::getMeasureCount).sum();
        int danger = 0;
        int warn = 0;
        int systolicGoal = -1, diastolicGoal = -1;

        List<BpHistory> histories = statistics.stream().map(BpSummary::getHistories).flatMap(Collection::stream).toList();

        for (BpHistory history : histories) {
            List<BloodPressure> bloodPressures = List.of(history.getFirstBP(), history.getSecondBP());

            for (BloodPressure bloodPressure : bloodPressures) {

                int systolic = bloodPressure.getSystolic();
                int diastolic = bloodPressure.getDiastolic();

                if(treatment != null) {
                    if(systolic < goalSystolic) systolicGoal++;
                    if(diastolic < goalDiastolic) diastolicGoal++;
                }

                if( (135 <= systolic && systolic < 155) || (85 <= diastolic && diastolic < 105)) warn++;

                if(155 <= systolic || 105 <= diastolic) danger++;

            }

        }

        return new BPReachRateVO(
                (danger * 100) / totalCount,
                (warn * 100) / totalCount,
                new BPSummaryVO(systolicGoal == -1 ? -1 : (systolicGoal * 100) / totalCount, diastolicGoal == -1 ? -1 : (diastolicGoal * 100) / totalCount)
        );
    }

    /**
     * @param statistics : 각 혈압 일 평균 데이터
     * @description 각 혈압 맥압 계산
     * @return : 맥압
     */
    public static double getPulsePressure(List<BpSummary> statistics) {

        BPSummaryVO averages = getAverages(statistics);

        return averages.getSystolicSummary() - averages.getDiastolicSummary();
    }

    /**
     * @param statistics : 각 혈압 일 평균 데이터
     * @description 각 혈압 도달률 계산
     * @return : 평균 동맥압
     */
    public static double getArterialPressure(List<BpSummary> statistics) {

        BPSummaryVO averages = getAverages(statistics);

        return (averages.getSystolicSummary() + ( 2 * averages.getDiastolicSummary())) / 3;
    }

    /**
     * @param treatment : 진료 상세 정보
     * @param statistics : 각 혈압 일 평균 데이터
     * @description 심뇌혈관질환 위험도 계산
     * @return : 심뇌혈관질환 위험도
     */
    public static CardiovascularDiseaseRisk getCardiovascularDisease(Treatment treatment, List<BpSummary> statistics) {

        CardiovascularDiseaseRisk result = CardiovascularDiseaseRisk.NORMAL;
        Patient patient = treatment.getPatient();
        int npeCount = 0;

        List<Predicate> conditions = List.of(isHasSomeDiseases(), isHasIssueAboutUrine(), isHasLvh(), isHasIssuePerAge(patient));

        try {
            for (Predicate p : conditions) {
                if (p.test(treatment)) {
                    result = CardiovascularDiseaseRisk.DANGEROUS;
                }
            }
        }
        catch (NullPointerException npe) {
            npeCount++;
        }

        if(result != CardiovascularDiseaseRisk.DANGEROUS) {
            result = npeCount == 0 ? result : CardiovascularDiseaseRisk.CAN_NOT_JUDGE;
        }

        return result;

    }

    static Predicate<Treatment> isHasSomeDiseases() {
        return (t) -> t.isHasPAOD() || t.isHasCVA() || t.isHasHF() || t.isHasAneurysm();
    }


    static Predicate<Treatment> isHasLvh() {
        return (t) -> t.getLvh();
    }

    static Predicate<Treatment> isHasIssueAboutUrine() {
        return (t) -> t.isHasCKD() && (t.getUProt() != ProtLevel.NEGATIVE || t.getUAlbDCr() > 0.2);
    }

    static Predicate<Treatment> isHasIssuePerAge(Patient patient) {

        return (t) -> {
            int age = patient.getAge();
            Gender gender = patient.getGender();
            int count = 0;

            if(ageCheck(gender, age)) count++;
            if( patient.isSmoke() ) count++;
            if( patient.getBMI() <= 25) count++;
            if( t.getTChol() >= 220 || t.getLdl() >= 150 || t.getHdl() < 40 || t.getTg() >= 220) count++;
            if( 100 <= t.getFbs() || t.getFbs() <= 126) count++;

            if(age < 65) return count >= 3;
            else         return count >= 2;

        };

    }

    /**
     * @param treatment : 진료 상세 정보
     * @param statistics : 각 혈압 일 평균 데이터
     * @description 목표 LDL
     * @return : 목표 LDL
     */
    public static int getGoalLDL(Treatment treatment, List<BpSummary> statistics) {

        final int HIGH = 130;
        final int MIDDLE = 100;
        final int LOW = 70;
        final int CAN_NOT_JUDGE = -1;

        int result = CAN_NOT_JUDGE;

        Patient patient = treatment.getPatient();

        int count = 0;

        try {
            if(ageCheck(patient.getGender(), patient.getAge())) count++;
            if( patient.isSmoke() ) count++;
            if( patient.isFamilyDisease()) count++;
            if( treatment.isHasHypertension()) count++;
            if( treatment.getHdl() < 40) count++;
        }
        catch (NullPointerException npe) {

        }

        boolean isHasDiseaseAboutLDL = treatment.isHasCAD() || treatment.isHasPAOD() || treatment.isHasCVA();
        boolean isHasHeavyDiseaseAboutLDL = treatment.isHasAneurysm() || treatment.isHasDM();
        boolean isHaveAnyIssue = count >= 2;

        if(isHasDiseaseAboutLDL) result = Math.max(result, LOW);
        if(isHasHeavyDiseaseAboutLDL) result = Math.max(result, MIDDLE);
        if(isHaveAnyIssue) result = Math.max(result, HIGH);

        return result;

    }


    /**
     * @param gender : 성별
     * @param age : 나이
     * @description
     * @return :
     */
    private static boolean ageCheck(Gender gender, int age) {
        return (gender == Gender.MALE && age >= 45) ||
                (gender == Gender.FEMALE && age >= 55);
    }

    /**
     * @param treatment : 진료 상세 정보
     * @param statistics : 각 혈압 일 평균 데이터
     * @description 혈압 조절 상태 조절 계산
     * @return : 혈압 조절 상태 조절
     */
    public static BPControl getBPControl(Treatment treatment, List<BpSummary> statistics) {

        int systolicAvg = (int)statistics.stream().mapToInt(i -> i.getBloodPressure().getSystolic()).average().orElse(0);
        int diastolicAvg = (int)statistics.stream().mapToInt(i -> i.getBloodPressure().getDiastolic()).average().orElse(0);

        return switch (treatment.getCardiovascularDiseaseRisk()) {
            case DANGEROUS ->
                (systolicAvg < 130 && (55 < diastolicAvg || diastolicAvg < 80)) ? BPControl.APPROPRIATE : BPControl.INAPPROPRIATE;
            case NORMAL ->
                (systolicAvg < 135 && (55 < diastolicAvg || diastolicAvg < 85)) ? BPControl.APPROPRIATE : BPControl.INAPPROPRIATE;
            case CAN_NOT_JUDGE -> BPControl.INAPPROPRIATE;
        };
    }

    /**
     * @param treatment : 진료 상세 정보
     * @param statistics : 각 혈압 일 평균 데이터
     * @description 약제 조절 정보
     * @return : 약제 조절 정보
     */
    public static MedicationControl getMedicationControl(Treatment treatment, List<BpSummary> statistics) {

        int systolicAvg = (int)statistics.stream().mapToInt(i -> i.getBloodPressure().getSystolic()).average().orElse(0);
        int diastolicAvg = (int)statistics.stream().mapToInt(i -> i.getBloodPressure().getDiastolic()).average().orElse(0);

        return switch (treatment.getCardiovascularDiseaseRisk()) {
            case NORMAL -> judgementMedicationControlForNormal(systolicAvg, diastolicAvg);
            case DANGEROUS -> judgementMedicationControlForDangerous(systolicAvg, diastolicAvg);
            case CAN_NOT_JUDGE -> MedicationControl.CAN_NOT_JUDGE;
        };

    }

    private static MedicationControl judgementMedicationControlForDangerous(int systolicAvg, int diastolicAvg) {
        if(systolicAvg >= 140 || diastolicAvg >= 85) return MedicationControl.INCREASE;
        else if(systolicAvg <90 || diastolicAvg< 60) return MedicationControl.DECREASE;
        else return MedicationControl.STAY;
    }

    private static MedicationControl judgementMedicationControlForNormal(int systolicAvg, int diastolicAvg) {
        if(systolicAvg >= 145 || diastolicAvg >= 90) return MedicationControl.INCREASE;
        else if(systolicAvg < 90 || diastolicAvg < 60) return MedicationControl.DECREASE;
        else return MedicationControl.STAY;
    }


    /**
     * description 주차 별 측정 상태 계산
     * @return
     */
    public boolean 주차별측정상태_updateWeeklyMeasureStatus(Patient patient) {
        return false;
    }

    public int 병원별가입자_updateHospitalRegisterCount(Long hospitalSeq) {
        return 0;
    }

    public int 병원별측정자_updateHospitalMeasuredCount(Hospital hospital) {
        return 0;
    }

    public int 병원별누적예측자_updateHospitalPredictorCount(Long hospitalSeq) {
        return 0;
    }

}