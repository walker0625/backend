package kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.dto.req.DiagnoseCommentModifyReq;
import kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.dto.req.DiagnoseResultReq;
import kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.dto.res.DiagnoseDetailRes;
import kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.dto.res.DiagnoseListForPatientRes;
import kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.dto.res.DiagnosePatientRes;
import kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.dto.res.DiagnoseResultRes;
import kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.service.DiagnoseService;
import kr.lifesemantics.canofymd.moduleapi.global.config.security.SecurityUserDetails;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "Bearer Authentication")
@SecurityScheme( name = "Bearer Authentication", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "Bearer")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/scai/diagnoses")
@Tag(name = "[SCAI]피부암 진단 정보 API", description = "피부암 진단 관리 목적의 API Docs")
public class DiagnoseController {

    DiagnoseService diagnoseService;

    @Operation(summary = "진단 이미지 및 정보 전송", description = "첫/동일병변 진단 둘다 사용")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = DiagnoseResultRes.class))}),
            @ApiResponse(responseCode = "404", description = """
                                                            responseStatus.code
                                                            \n7008 -> 진단 조회 실패
                                                            """),
            @ApiResponse(responseCode = "500", description = """
                                                            responseStatus.code
                                                            \n7001 -> 모델 전송후 예측 실패
                                                            \n7002 -> 원본 이미지 저장 실패
                                                            \n7003 -> 썸네일 이미지 저장 실패
                                                            """)
    })
    @PostMapping
    public ResponseEntity diagnose(@ModelAttribute DiagnoseResultReq diagnoseResultReq) {

        return ResponseEntity.ok().body(diagnoseService.diagnose(diagnoseResultReq));
    }

    @Operation(summary = "진단 상세 조회", description = "전송 이미지 및 이전 진단 함께 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = DiagnoseDetailRes.class))}),
            @ApiResponse(responseCode = "404", description = """
                                                            responseStatus.code
                                                            \n4002 -> 해당 환자가 없음
                                                            \n7008 -> 진단 조회 실패
                                                            """),
            }
    )
    @GetMapping("/{diagnoseSeq}")
    public ResponseEntity diagnoseDetail(@PathVariable Long diagnoseSeq) {

        return ResponseEntity.ok().body(diagnoseService.diagnoseDetail(diagnoseSeq));
    }

    @Operation(summary = "진단 코멘트 수정", description = "진단 상세 내 코멘트 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
        }
    )
    @PatchMapping("/{diagnoseSeq}")
    public ResponseEntity diagnoseCommentModify(@PathVariable Long diagnoseSeq,
                                                @RequestBody DiagnoseCommentModifyReq req) {

        diagnoseService.diagnoseCommentModify(diagnoseSeq, req.getComment());

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "감별 내역 목록 조회", description = "로그인한 의사가 관리하는 환자들의 지난 감별 내역 목록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = DiagnosePatientRes.class))}),
    }
    )
    @GetMapping
    public ResponseEntity diagnoseList(@AuthenticationPrincipal SecurityUserDetails securityUserDetails,
                                       @RequestParam(defaultValue = "") String keyword,
                                       Pageable pageable) {

        return ResponseEntity.ok().body(diagnoseService.diagnoseList(securityUserDetails.getSeq(), pageable, keyword));
    }

    @Operation(summary = "환자별 이전 감별 내역 목록", description = "환자별 이전 감별 내역 목록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = DiagnoseListForPatientRes.class))}),
    }
    )
    @GetMapping("/patient/{patientSeq}")
    public ResponseEntity getDiagnosesForPatient(@PathVariable Long patientSeq) {

        return ResponseEntity.ok(diagnoseService.getDiagnosesForPatient(patientSeq));
    }

}