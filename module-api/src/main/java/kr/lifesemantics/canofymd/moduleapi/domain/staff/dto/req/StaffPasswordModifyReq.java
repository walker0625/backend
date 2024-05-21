package kr.lifesemantics.canofymd.moduleapi.domain.staff.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(title = "병원관계자 비밀번호 수정 요청 DTO")
public class StaffPasswordModifyReq {

    @Schema(description = "병원관계자 순차번호", example = "1")
    Long staffSeq;

    @Schema(description = "수정할 비밀번호(원문)", example = "qwe123!@#")
    String password;

}