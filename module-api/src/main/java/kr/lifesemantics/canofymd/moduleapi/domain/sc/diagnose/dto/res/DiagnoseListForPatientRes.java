package kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.res.PatientBasicInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.dto.res
 * fileName       : DiagnoseListForPatientRes
 * author         : ms.jo
 * date           : 2024/05/16
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/05/16        ms.jo       최초 생성
 */
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(title = " DTO")
@AllArgsConstructor
public class DiagnoseListForPatientRes {

    @Schema(description = "환자 기본 정보")
    PatientBasicInfo basicInfo;
    @Schema(description = "감별내역")
    List<DiagnoseHistoryRes> histories;

}
