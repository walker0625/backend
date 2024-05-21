package kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.dto.res;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class DiagnoseEntity {

    List<Double> probability;
    CancerInfo className;
    boolean predictionConfidence;

}
