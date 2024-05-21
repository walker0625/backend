package kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.lifesemantics.canofymd.modulecore.domain.bp.value.BloodPressure;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(title = "혈압 기록 상세 응답 DTO")
public class BpHistoryDetailRes {

    @Schema(description = "혈압 기록 순차번호", example = "1")
    Long seq;

    @Schema(description = "첫번째 혈압", example = "{\"systolic\": 120, \"diastolic\": 80, \"pulse\": 70}")
    BloodPressure firstBP;

    @Schema(description = "두번째 혈압", example = "{\"systolic\": 120, \"diastolic\": 80, \"pulse\": 70}")
    BloodPressure secondBP;

    @Schema(description = "측정일시", example = "2024-04-26T02:19:00")
    LocalDateTime measureTime;

}