package kr.lifesemantics.canofymd.moduleapi.domain.treatment.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.treatment.dto.req
 * fileName       : TreatmentFindListRes
 * author         : ms.jo
 * date           : 2024/04/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/04/26        ms.jo       최초 생성
 */

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(title = "진료 리스트 조회 결과 DTO")
@AllArgsConstructor
public class TreatmentFindListRes {

    @Schema(description = "진료 순차번호", example = "1")
    Long treatmentSeq;

    @Schema(description = "진료 일자", example = "2024-04-14")
    LocalDate treatedAt;

}
