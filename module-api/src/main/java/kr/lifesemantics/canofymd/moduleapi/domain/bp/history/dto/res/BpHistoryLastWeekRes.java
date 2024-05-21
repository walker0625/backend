package kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.lifesemantics.canofymd.modulecore.domain.bp.value.BloodPressure;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(title = "지난주 혈압 최고/최저 응답 DTO")
public class BpHistoryLastWeekRes {

    @Schema(description = "최고 혈압", example = "{\"systolic\": 120, \"diastolic\": 80, \"pulse\": 70}")
    BloodPressure maxBp;

    @Schema(description = "최저 혈압", example = "{\"systolic\": 120, \"diastolic\": 80, \"pulse\": 70}")
    BloodPressure lowestBp;

}
