package kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.lifesemantics.canofymd.modulecore.domain.bp.value.BloodPressure;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Schema(title = "혈압 기록 등록 요청 DTO")
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class BpHistoryRegisterReq {

    @JsonIgnore
    @Schema(description = "환자 순차번호(APP은 보내지 않아도 됨)", example = "1")
    private Long patientSeq;

    @Schema(description = "첫번째 혈압", example = "{\"systolic\": 120, \"diastolic\": 80, \"pulse\": 70}")
    private BloodPressure firstBP;

    @Schema(description = "두번째 혈압", example = "{\"systolic\": 120, \"diastolic\": 80, \"pulse\": 70}")
    private BloodPressure secondBP;

    @Schema(description = "측정일시", example = "2024-04-26T02:19:00")
    @DateTimeFormat(pattern = "YYYY-mm-ddTHH:MM:ss")
    private LocalDateTime measureTime;

    @JsonIgnore
    @Schema(description = "유효혈압 여부(4AM ~ 12PM)", example = "true")
    private boolean isUsable;

}