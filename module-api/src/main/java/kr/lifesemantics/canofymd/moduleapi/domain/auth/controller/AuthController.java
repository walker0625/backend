package kr.lifesemantics.canofymd.moduleapi.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.lifesemantics.canofymd.moduleapi.domain.auth.dto.req.JwtIssueReq;
import kr.lifesemantics.canofymd.moduleapi.domain.auth.dto.res.ChangePwYNRes;
import kr.lifesemantics.canofymd.moduleapi.domain.auth.dto.res.JwtIssueRes;
import kr.lifesemantics.canofymd.moduleapi.domain.auth.service.AuthService;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.res.BpHistoryLastWeekRes;
import kr.lifesemantics.canofymd.moduleapi.global.config.security.SecurityUserDetails;
import kr.lifesemantics.canofymd.modulecore.enums.AuthType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "Bearer Authentication")
@SecurityScheme( name = "Bearer Authentication", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "Bearer")
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "인증 관리 API", description = "인증(web/app) 관리 API Docs")
public class AuthController {

    AuthService authService;

    @Operation(summary = "JWT 발급(병원 관계자)", description = " id/password 바탕으로 APP JWT(만료:1일) 발급")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = JwtIssueRes.class))}),
        @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/staff")
    public ResponseEntity issuesStaffJwt(@RequestBody JwtIssueReq jwtIssueReq) {

        return ResponseEntity.ok().body(authService.issueStaffJwt(jwtIssueReq.getId(), jwtIssueReq.getPassword()));
    }

    @Operation(summary = "JWT 발급(환자)", description = "id/password 바탕으로 APP JWT(만료:1일) 발급")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = JwtIssueRes.class))}),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/patient")
    public ResponseEntity issuePatientJwt(@RequestBody JwtIssueReq jwtIssueReq) {

        return ResponseEntity.ok().body(authService.issuePatientJwt(
                jwtIssueReq.getId(),
                jwtIssueReq.getPassword(),
                jwtIssueReq.getPushToken()));
    }

    @Operation(summary = "비밀번호 변경 여부 확인(병원관계자-scai app)", description = "이미 변경 했으면 true, 아직이면 false 응답(비밀번호 변경 창으로 이동)")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "이미 변경 했으면 true, 아직이면 false 응답(비밀번호 변경 창으로 이동)", content = {@Content(schema = @Schema(implementation = ChangePwYNRes.class))})
    })
    @GetMapping("/check-pw-change/staff")
    public ResponseEntity checkAlreadyChangePasswordForStaff(@AuthenticationPrincipal SecurityUserDetails details) {

        return ResponseEntity.ok().body(authService.checkAlreadyChangePassword(AuthType.STAFF, details.getSeq()));
    }

    @Operation(summary = "비밀번호 변경 여부 확인(환자-bpai app)", description = "이미 변경 했으면 true, 아직이면 false 응답(비밀번호 변경 창으로 이동)")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "이미 변경 했으면 true, 아직이면 false 응답(비밀번호 변경 창으로 이동)", content = {@Content(schema = @Schema(implementation = ChangePwYNRes.class))})
    })
    @GetMapping("/check-pw-change/patient")
    public ResponseEntity checkAlreadyChangePasswordForPatient(@AuthenticationPrincipal SecurityUserDetails details) {

        return ResponseEntity.ok().body(authService.checkAlreadyChangePassword(AuthType.PATIENT, details.getSeq()));
    }


}