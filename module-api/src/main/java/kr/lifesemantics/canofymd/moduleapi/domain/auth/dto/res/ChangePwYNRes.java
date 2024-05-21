package kr.lifesemantics.canofymd.moduleapi.domain.auth.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(title = "비밀번호 변경 여부 응답 DTO")
public class ChangePwYNRes {

    @Schema(description = "비밀번호 변경 여부", example = "true")
    private boolean isPasswordChange;

}
