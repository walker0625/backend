package kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.lifesemantics.canofymd.modulecore.domain.bp.value.BloodPressure;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(title = "혈압 그래프 응답 DTO")
public class BpHistoryGraphRes {

    @Schema(description = "측정일", example = "2024-04-26")
    String date;

    @Schema(description = "평균 혈압", example = "{\"systolic\": 120, \"diastolic\": 80, \"pulse\": 70}")
    BloodPressure bp;

}