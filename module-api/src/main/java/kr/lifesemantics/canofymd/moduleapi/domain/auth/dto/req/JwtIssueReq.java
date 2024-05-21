package kr.lifesemantics.canofymd.moduleapi.domain.auth.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.lifesemantics.canofymd.modulecore.enums.Category;
import lombok.Data;

@Data
@Schema(title = "JWT 발급 요청 DTO")
public class JwtIssueReq {

    @Schema(description = "아이디", example = "doctor1")
    private String id;

    @Schema(description = "비밀번호", example = "qwe123!@#")
    private String password;

    @Schema(description = "푸시토큰", example = "qwehfdsiufhsdkfjhdskfhsdfdshf213u21swjhfksj893")
    private String pushToken;



}
