package kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.mapper;

import kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.res.PatientBasicInfo;
import kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.dto.res.DiagnoseHistoryRes;
import kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.dto.res.DiagnoseListForPatientRes;
import kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.dto.res.DiagnoseImage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.mapper
 * fileName       : DiagnoseMapper
 * author         : ms.jo
 * date           : 2024/05/16
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/05/16        ms.jo       최초 생성
 */
@Configuration
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DiagnoseMapper {

    public static DiagnoseListForPatientRes toDiagnoseListForPatientRes(PatientBasicInfo basicInfo, List<DiagnoseHistoryRes> diagnoses) {
        return new DiagnoseListForPatientRes(basicInfo, diagnoses);
    }

    public DiagnoseImage toDiagnoseImage(Long diagnoseSeq, String url, Double cancerRate, Double nonCancerRate, LocalDateTime createdAt) {
        return new DiagnoseImage(diagnoseSeq, url, cancerRate, nonCancerRate, createdAt);
    }

}
