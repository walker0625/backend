package kr.lifesemantics.canofymd.moduleapi.domain.hospital.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "병원 필터용 정보 조회 응답 DTO ")
public class HospitalFilterRes {

    @Schema(description = "병원 순차번호", example = "1")
    Long hospitalSeq;

    @Schema(description = "병원 이름", example = "병원1")
    String name;

}
