package kr.lifesemantics.canofymd.moduleapi.domain.treatment.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.lifesemantics.canofymd.modulecore.enums.ProtLevel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.treatment.dto.req
 * fileName       : TreatmentModifyReq
 * author         : ms.jo
 * date           : 2024/04/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/04/26        ms.jo       최초 생성
 */

@Schema(title = "진료 기록 수정 요청 DTO")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class TreatmentModifyReq {

    @Schema(description = "hasCAD 여부", example = "true")
    boolean hasCAD;
    @Schema(description = "hasPAOD 여부", example = "true")
    boolean hasPAOD;
    @Schema(description = "hasCVA 여부", example = "true")
    boolean hasCVA;
    @Schema(description = "hasCKD 여부", example = "true")
    boolean hasCKD;
    @Schema(description = "hasHF 여부", example = "true")
    boolean hasHF;
    @Schema(description = "hasAneurysm 여부", example = "true")
    boolean hasAneurysm;
    @Schema(description = "hasHypertension 여부", example = "true")
    boolean hasHypertension;
    @Schema(description = "hasHL 여부", example = "true")
    boolean hasHL;
    @Schema(description = "hasDM 여부", example = "true")
    boolean hasDM;

    @Schema(description = "진료 검사 여부", example = "true")
    boolean hasMeasurement;
    @Schema(description = "진료 검사일", example = "2024-04-14")
    LocalDate measuredDate;
    @Schema(description = "수축기 혈압", example = "144")
    Integer systolic;
    @Schema(description = "이완기 혈압", example = "84")
    Integer diastolic;

    @Schema(description = "혈액 검사 여부", example = "true")
    boolean hasBloodCheck;
    @Schema(description = "혈액 검사일", example = "2024-04-14")
    LocalDate bloodCheckDate;
    @Schema(description = "tChol 수치", example = "84")
    Integer tChol;
    @Schema(description = "tg 수치", example = "84")
    Integer tg;
    @Schema(description = "hdl 수치", example = "84")
    Integer hdl;
    @Schema(description = "ldl 수치", example = "84")
    Integer ldl;
    @Schema(description = "fbs 수치", example = "84")
    Integer fbs;

    @Schema(description = "소변 검사 여부", example = "true")
    boolean hasUrineTest;
    @Schema(description = "소변 검사일", example = "2024-04-14")
    LocalDate urineTestDate;
    @Schema(description = "uPot 수치", example = "ONE_PLUS")
    ProtLevel uProt;
    @Schema(description = "uAlbDCr 수치", example = "44")
    Integer uAlbDCr;

    @Schema(description = "심전도 검사 여부", example = "true")
    boolean hasElectrocardiogramTest;
    @Schema(description = "심전도 검사일", example = "2024-04-14")
    LocalDate electrocardiogramTestDate;
    @Schema(description = "lvh 여부", example = "true")
    boolean lvh;

    @Schema(description = "진료일", example = "2024-04-14")
    LocalDate treatedAt;
}
