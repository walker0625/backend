package kr.lifesemantics.canofymd.moduleapi.domain.treatment.service;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.lifesemantics.canofymd.modulecore.enums.BPControl;
import kr.lifesemantics.canofymd.modulecore.enums.CardiovascularDiseaseRisk;
import kr.lifesemantics.canofymd.modulecore.enums.MedicationControl;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.treatment.service
 * fileName       : TreatmentAnalyticsRes
 * author         : ms.jo
 * date           : 2024/05/02
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/05/02        ms.jo       최초 생성
 */

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(title = "진료 내역 분석 결과 DTO")
@AllArgsConstructor
public class TreatmentAnalyticsRes {

    @Schema(description = "심뇌혈관 위험도", example = "NORMAL")
    CardiovascularDiseaseRisk diseaseRisk;
    @Schema(description = "목표 수축기 혈압", example = "130")
    int goalSystolic;
    @Schema(description = "목표 이완기 혈압", example = "80")
    int goalDiastolic;
    @Schema(description = "목표 LDL 콜레스테롤", example = "70")
    int goalLDL;
    @Schema(description = "혈압 조절", example = "APPROPRIATE")
    BPControl bpControl;
    @Schema(description = "약제 조절", example = "INCREASE")
    MedicationControl medicationControl;

}
