package kr.lifesemantics.canofymd.moduleapi.domain.treatment.mapper;

import kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.res.PatientBasicInfo;
import kr.lifesemantics.canofymd.moduleapi.domain.treatment.dto.res.TreatmentFindListRes;
import kr.lifesemantics.canofymd.moduleapi.domain.treatment.dto.res.TreatmentFindRes;
import kr.lifesemantics.canofymd.moduleapi.domain.treatment.service.TreatmentAnalyticsRes;
import kr.lifesemantics.canofymd.modulecore.domain.bp.Treatment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.treatment.mapper
 * fileName       : TreatmentMapper
 * author         : ms.jo
 * date           : 2024/04/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/04/26        ms.jo       최초 생성
 */

@Configuration
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TreatmentMapper {


    public TreatmentFindRes toTreatmentFindRes(PatientBasicInfo basicInfo, Treatment treatment) {
        return new TreatmentFindRes(
                basicInfo,
                treatment.isHasCAD(), treatment.isHasPAOD(), treatment.isHasCVA(), treatment.isHasCKD(), treatment.isHasHF(),
                treatment.isHasAneurysm(), treatment.isHasHypertension(), treatment.isHasHL(), treatment.isHasDM(),
                treatment.isHasMeasurement(), treatment.getMeasuredDate(), treatment.getSystolic(), treatment.getDiastolic(),
                treatment.isHasBloodCheck(), treatment.getBloodCheckDate(), treatment.getTChol(), treatment.getTg(), treatment.getHdl(), treatment.getLdl(), treatment.getFbs(),
                treatment.isHasUrineTest(), treatment.getUrineTestDate(), treatment.getUProt(), treatment.getUAlbDCr(),
                treatment.isHasElectrocardiogramTest(), treatment.getElectrocardiogramTestDate(), treatment.getLvh(), treatment.getTreatedAt());
    }

    public TreatmentFindListRes toTreatmentFindListRes(Treatment treatment) {
        return new TreatmentFindListRes(treatment.getSeq(), treatment.getTreatedAt());
    }

    public TreatmentAnalyticsRes toTreatmentAnalyticsRes(Treatment treatment) {
        return new TreatmentAnalyticsRes(
                treatment.getCardiovascularDiseaseRisk(),
                treatment.getGoalSystolic(), treatment.getGoalDiastolic(), treatment.getGoalLDL(),
                treatment.getBpControl(), treatment.getMedicationControl());

    }
}
