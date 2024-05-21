package kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.lifesemantics.canofymd.modulecore.domain.bp.value.BloodPressure;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Schema(title = "혈압 기록 삭제 요청 DTO")
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class BpHistoryDeleteReq {

    @Schema(description = "혈압 기록 순차번호", example = "1")
    private Long seq;

}