package kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.dto.res;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class DiagnoseImage {

    Long diagnosSeq;
    String imageUrl;
    double cancerRate;
    double nonCancerRate;
    LocalDateTime diagnoseAt;

}
