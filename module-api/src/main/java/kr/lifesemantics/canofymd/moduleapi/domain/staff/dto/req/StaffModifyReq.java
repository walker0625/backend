package kr.lifesemantics.canofymd.moduleapi.domain.staff.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.lifesemantics.canofymd.modulecore.enums.UserType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(title = "병원관계자 수정 요청 DTO")
@NoArgsConstructor
public class StaffModifyReq {

    @Schema(description = "병원관계자 순차번호", example = "1")
    Long staffSeq;

    @Schema(description = "이름", example = "김코디")
    String name;

    @Schema(description = "사용자 구분", example = "DOCTOR")
    UserType userType;

    @Schema(description = "연락처", example = "010-1111-1111")
    String contact;

    @Schema(description = "활성화 여부", example = "true")
    Boolean isActive;

}