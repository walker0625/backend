package kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.res.PatientBasicInfo;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.dto.res
 * fileName       : DiagnosePatientRes
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
@Schema(title = "의사 별 환자 감별 내역 DTO")
@AllArgsConstructor
public class DiagnosePatientRes {

    PatientBasicInfo basicInfo;
    DiagnoseHistoryRes diagnoseHistory;

}
