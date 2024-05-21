package kr.lifesemantics.canofymd.moduleapi.domain.treatment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.res.PatientFindListRes;
import kr.lifesemantics.canofymd.moduleapi.domain.treatment.dto.req.TreatmentModifyReq;
import kr.lifesemantics.canofymd.moduleapi.domain.treatment.dto.req.TreatmentRegisterReq;
import kr.lifesemantics.canofymd.moduleapi.domain.treatment.dto.res.TreatmentFindListRes;
import kr.lifesemantics.canofymd.moduleapi.domain.treatment.dto.res.TreatmentFindRes;
import kr.lifesemantics.canofymd.moduleapi.domain.treatment.service.TreatmentAnalyticsRes;
import kr.lifesemantics.canofymd.moduleapi.domain.treatment.service.TreatmentService;
import kr.lifesemantics.canofymd.moduleapi.global.config.security.SecurityUserDetails;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.treatment
 * fileName       : TreatmentController
 * author         : ms.jo
 * date           : 2024/04/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/04/26        ms.jo       최초 생성
 */

@SecurityRequirement(name = "Bearer Authentication")
@SecurityScheme( name = "Bearer Authentication", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "Bearer")
@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "진료 정보 API", description = "진료 관리 목적의 API Docs")
@RequestMapping("/treatments")
public class TreatmentController {

    TreatmentService treatmentService;

    @ResponseBody
    @Operation(summary = "진료 등록", description = "진료 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = PatientFindListRes.class))}),
            @ApiResponse(responseCode = "400", description = """
                                                            진료 등록 실패
                                                            """
            )
    })
    @PostMapping
    public ResponseEntity create(@RequestBody TreatmentRegisterReq request,
                                 @AuthenticationPrincipal SecurityUserDetails securityUserDetails) {
        return ResponseEntity.ok().body(treatmentService.create(securityUserDetails.getHospitalSeq(), request));
    }

    @Operation(summary = "환자 진료 목록 조회", description = "환자 진료 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = TreatmentFindListRes.class))})
    })
    @GetMapping("/patients/{patientSeq}")
    public ResponseEntity getTreatmentsByPatient(@AuthenticationPrincipal SecurityUserDetails userDetails,
                                           @PathVariable Long patientSeq) {

        return ResponseEntity.ok(treatmentService.findTreatmentFindResListByPatient(patientSeq));

    }

    @Operation(summary = "진료 상세 조회", description = "진료 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = TreatmentFindRes.class))})
    })
    @GetMapping("/{treatmentSeq}")
    public ResponseEntity getTreatmentDetail(@AuthenticationPrincipal SecurityUserDetails userDetails,
                                           @PathVariable Long treatmentSeq) {

        return ResponseEntity.ok(treatmentService.getTreatmentDetail(treatmentSeq));

    }

    @Operation(summary = "진료 분석 조회", description = "진료 분석 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = TreatmentAnalyticsRes.class))})
    })
    @GetMapping("/analytics/patients/{patientSeq}")
    public ResponseEntity getAnalyticsTreatment(@PathVariable Long patientSeq) {

        return ResponseEntity.ok(treatmentService.getAnalyticsTreatment(patientSeq));
    }


    @Operation(summary = "진료 수정", description = "진료 상세 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = TreatmentFindRes.class))})
    })
    @PatchMapping("/{treatmentSeq}")
    public ResponseEntity modifyTreatment(@AuthenticationPrincipal SecurityUserDetails userDetails,
                                           @PathVariable Long treatmentSeq,
                                          @RequestBody TreatmentModifyReq request) {

        return ResponseEntity.ok(treatmentService.modifyTreatment(treatmentSeq, request));

    }

    @Operation(summary = "진료 내역 삭제", description = "진료 내역 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    @DeleteMapping("/{treatmentSeq}")
    public ResponseEntity deleteTreatment(@AuthenticationPrincipal SecurityUserDetails userDetails,
                                           @PathVariable Long treatmentSeq) {

        treatmentService.deleteTreatment(treatmentSeq);

        return ResponseEntity.ok().build();

    }

}
