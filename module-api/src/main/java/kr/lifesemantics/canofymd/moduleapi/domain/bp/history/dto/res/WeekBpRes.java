package kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.lifesemantics.canofymd.modulecore.domain.bp.value.BloodPressure;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(title = "기간 조회 응답 DTO")
public class WeekBpRes {

    List<BpHistoryGraphRes> graphBpList;
    BloodPressure maxBp;
    BloodPressure minBp;
    BloodPressure averageBp;

    @Schema(description = "평균 맥압", example = "32.3")
    double pulsePressure;
    @Schema(description = "평균 동맥압", example = "33")
    double arterialPressure;

}
