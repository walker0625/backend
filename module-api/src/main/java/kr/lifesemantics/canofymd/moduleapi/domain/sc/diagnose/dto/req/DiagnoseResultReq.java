package kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.dto.req;

import kr.lifesemantics.canofymd.modulecore.enums.Gender;
import kr.lifesemantics.canofymd.modulecore.enums.Spot;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiagnoseResultReq {

    Long patientSeq;
    MultipartFile image;
    Spot spot;
    String comment;

    // 동일 병변 전송시 사용
    Long diagnoseSeq;
    boolean isSameGroup;

}
