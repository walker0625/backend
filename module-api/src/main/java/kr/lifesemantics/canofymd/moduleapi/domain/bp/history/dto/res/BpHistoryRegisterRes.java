package kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.lifesemantics.canofymd.modulecore.domain.bp.value.BloodPressure;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
@Schema(title = "혈압기록 등록 응답 DTO")
public class BpHistoryRegisterRes {

    @Schema(description = "혈압 기록 순차번호", example = "1")
    Long seq;

    @Schema(description = "환자 순차번호", example = "1")
    Long patientSeq;

    @Schema(description = "첫번째 혈압", example = "{\"systolic\": 120, \"diastolic\": 80, \"pulse\": 70}")
    BloodPressure firstBP;

    @Schema(description = "두번째 혈압", example = "{\"systolic\": 120, \"diastolic\": 80, \"pulse\": 70}")
    BloodPressure secondBP;

    @Schema(description = "주차")
    int week;

    @Schema(description = "측정일", example = "2024-04-26")
    LocalDate measureDate;

    @Schema(description = "측정일시", example = "2024-04-26T02:19:00")
    LocalDateTime measureTime;

    @JsonProperty("isUsable")
    @Schema(description = "유효혈압 여부(4AM ~ 12PM)", example = "true")
    boolean isUsable;

}