package kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DiagnoseResultRes {

    Long diagnoseSeq;
    Boolean predictionConfidence;

    public static DiagnoseResultRes successRes(Long diagnoseSeq) {
        return DiagnoseResultRes.builder()
                .diagnoseSeq(diagnoseSeq)
                .build();
    }

    public static DiagnoseResultRes predictionConfidenceRes(boolean predictionConfidence) {
        return DiagnoseResultRes.builder()
                .predictionConfidence(predictionConfidence)
                .build();
    }

}
