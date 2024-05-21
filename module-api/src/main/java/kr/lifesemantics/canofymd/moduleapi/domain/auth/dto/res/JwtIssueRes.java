package kr.lifesemantics.canofymd.moduleapi.domain.auth.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(title = "JWT 발급 응답 DTO")
public class JwtIssueRes {

    @Schema(description = "Access Token(JWT)", example = "eyJhbGciOiJIUzI1NiJ9.eyJhdXRoVHlwZSI6IlNUQUZGIiwiaWQiOiJkb2N0b3IxIiwibmFtZSI6Iuq5gOydmOyCrCIsInNlcSI6MiwiaG9zcGl0YWxTZXEiOjEsImlhdCI6MTcxNTA1NjkxMCwiZXhwIjoxNzE1MTQzMzEwfQ.EfRhysxN5V-Wy3m36yerNnnup0isr-nq8bMTfIduwZM")
    private String accessToken;

}
