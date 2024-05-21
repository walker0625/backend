package kr.lifesemantics.canofymd.moduleapi.domain.patient.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.req.PatientModifyPasswordReq;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.req.PatientModifyReq;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.req.PatientRegisterReq;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.req.PatientUpdatePushTokenReq;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.res.*;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.service.PatientService;
import kr.lifesemantics.canofymd.moduleapi.global.config.security.SecurityUserDetails;
import kr.lifesemantics.canofymd.modulecore.enums.Category;
import kr.lifesemantics.canofymd.modulecore.enums.ParticipationCompliance;
import kr.lifesemantics.canofymd.modulecore.exception.BusinessException;
import kr.lifesemantics.canofymd.modulecore.util.ResponseStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.patient.repository
 * fileName       : PatientController
 * author         : ms.jo
 * date           : 2024/04/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/04/24        ms.jo       최초 생성
 */

@SecurityRequirement(name = "Bearer Authentication")
@SecurityScheme( name = "Bearer Authentication", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "Bearer")
@RestController
@RequestMapping("/{category}/patients")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequiredArgsConstructor
@Tag(name = "환자 정보 API")
public class PatientController {

    PatientService patientService;

    @PostMapping
    @ResponseBody
    @Operation(summary = "환자 등록", description = "환자 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = PatientFindListRes.class))}),
            @ApiResponse(responseCode = "400", description = """
                    responseStatus.code
                    \n2003 -> 병원 생성 시 문제 발생
                    """
            )
    })
    public ResponseEntity create(@RequestBody PatientRegisterReq request,
                                 @AuthenticationPrincipal SecurityUserDetails securityUserDetails,
                                 @Parameter() @PathVariable(name = "category", value = "category") String category) {

        checkCorrectCategory(securityUserDetails, category);

        return ResponseEntity.ok().body(patientService.create(securityUserDetails.getHospitalSeq(), securityUserDetails.getSeq(), getCategory(category), request));
    }

    private @NotNull Category getCategory(String category) {
        return Category.valueOf(category.toUpperCase());
    }

    @Operation(summary = "환자 목록", description = "환자 목록 조회(검색 키워드 : 이름)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = PatientFindListRes.class))})
    })
    @GetMapping("/hospital/{hospitalSeq}")
    public ResponseEntity getPatientsFromHospital(@AuthenticationPrincipal SecurityUserDetails userDetails,
                                                  @PathVariable Long hospitalSeq,
                                                  @PathVariable(name = "category", value = "category") String category,
                                                  @RequestParam(required = false) List<ParticipationCompliance> filters,
                                                  @RequestParam(defaultValue = "") String keyword,
                                                  Pageable pageable) {

        checkCorrectCategory(userDetails, category);

        return ResponseEntity.ok(patientService.getPatientsFromHospital(userDetails.getUserType(), getCategory(category), hospitalSeq, filters, keyword, pageable));

    }

    private void checkCorrectCategory(SecurityUserDetails userDetails, String category) {
        if(!userDetails.getCategory().stream().filter(i -> Objects.equals(i, getCategory(category))).findAny().isPresent()) {
            throw new BusinessException(ResponseStatus.INVALID_REQUEST_OF_TYPE, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "의료진 별 환자 목록", description = "의료진 별 환자 목록 조회(검색 키워드 : 이름)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = PatientFindByDoctorListRes.class))})
    })
    @GetMapping("/doctor/{doctorSeq}")
    public ResponseEntity getPatientsFromDoctor(@AuthenticationPrincipal SecurityUserDetails userDetails,
                                                  @PathVariable Long doctorSeq,
                                                  @PathVariable(name = "category", value = "category") String category,
                                                  @RequestParam(defaultValue = "") String keyword,
                                                  Pageable pageable) {

        Category categoryObject = getCategory(category);

        checkCorrectCategory(userDetails, category);

        return ResponseEntity.ok(patientService.getPatientsFromDoctor(categoryObject, doctorSeq, keyword, pageable));

    }

    @Operation(summary = "측정 독려 이용자 조회", description = "측정 독려 이용자 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = PatientParticipationDangerousListRes.class))})
    })
    @GetMapping("/hospital/{hospitalSeq}/status/dangerous")
    public ResponseEntity getParticipationDangerousPatients(@AuthenticationPrincipal SecurityUserDetails userDetails,
                                                            @PathVariable(name = "category", value = "category") String category,
                                                            @PathVariable Long hospitalSeq) {

        checkCorrectCategory(userDetails, category);

        return ResponseEntity.ok(patientService.getParticipationDangerousPatients(userDetails.getUserType(), hospitalSeq, getCategory(category)));

    }

    @PostMapping(value = {"/cheerUp", "/cheerUp/{patientSeq}"})
    @ResponseBody
    @Operation(summary = "측정 독려 환자 푸시 알림", description = "측정 독려 환자 푸시 알림")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "400", description = """
                responseStatus.code
                \n2003 -> 전송 시 문제 발생
                """
        )
    })
    public ResponseEntity sendCheerUp(
                                 @AuthenticationPrincipal SecurityUserDetails securityUserDetails,
                                 @PathVariable(required = false) Long patientSeq,
                                 @Parameter() @PathVariable(name = "category", value = "category") String category) {

        checkCorrectCategory(securityUserDetails, category);
        patientService.sendCheerUp(securityUserDetails.getUserType(), securityUserDetails.getHospitalSeq(), patientSeq, getCategory(category));
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "환자 상세 조회", description = "환자 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = PatientDetailRes.class))})
    })
    @GetMapping("/{patientSeq}")
    public ResponseEntity getPatientDetail(@AuthenticationPrincipal SecurityUserDetails userDetails,
                                           @PathVariable(name = "category", value = "category") String category,
                                           @PathVariable Long patientSeq) {

        checkCorrectCategory(userDetails, category);

        return ResponseEntity.ok(patientService.getPatientDetail(patientSeq));

    }

    @Operation(summary = "환자 인적사항 조회", description = "환자 인적사항 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = PatientPersonalRes.class))})
    })
    @GetMapping("/{patientSeq}/personal")
    public ResponseEntity getPatientPersonalDetail(@AuthenticationPrincipal SecurityUserDetails userDetails,
                                                   @PathVariable(name = "category", value = "category") String category,
                                                   @PathVariable Long patientSeq) {

        checkCorrectCategory(userDetails, category);

        return ResponseEntity.ok(patientService.getPatientPersonalDetail(patientSeq));

    }

    @Operation(summary = "환자 진료 시 기본정보 조회", description = "환자 진료 시 기본정보 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = PatientBasicInfo.class))})
    })
    @GetMapping("/{patientSeq}/basic")
    public ResponseEntity getPatientBasic(@AuthenticationPrincipal SecurityUserDetails userDetails,
                                          @PathVariable(name = "category", value = "category") String category,
                                           @PathVariable Long patientSeq) {

        checkCorrectCategory(userDetails, category);

        return ResponseEntity.ok(patientService.getBasicInfo(patientSeq));

    }

    @Operation(summary = "환자 ID 중복 확인", description = "환자 ID 중복 확인 (중복 시 false)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공")
    })

    @GetMapping("/duplicate")
    public ResponseEntity getParticipationDangerousPatients(String id,
                                                            @PathVariable(name = "category", value = "category") String category) {

        return ResponseEntity.ok(patientService.duplicateCheckId(id));

    }

    @ResponseBody
    @Operation(summary = "환자 수정", description = "환자 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = """
                                                            환자 정보 수정 실패
                                                            """
            )
    })
    @PatchMapping("/{patientSeq}")
    public ResponseEntity modify(@PathVariable Long patientSeq,
                                 @PathVariable(name = "category", value = "category") String category,
                                 @RequestBody PatientModifyReq request) {
        patientService.modify(patientSeq, request);

        return ResponseEntity.ok().build();
    }

    @ResponseBody
    @Operation(summary = "환자 푸시토큰 업데이트", description = "환자 푸시토큰 업데이트")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = """
                                                            환자 푸시토큰 업데이트
                                                            """
            )
    })
    @PatchMapping("/{patientSeq}/push-token")
    public ResponseEntity updatePushToken(@PathVariable Long patientSeq,
                                          @PathVariable(name = "category", value = "category") String category,
                                          @RequestBody PatientUpdatePushTokenReq request) {
        patientService.updatePushToken(patientSeq, request);

        return ResponseEntity.ok().build();
    }


    @ResponseBody
    @Operation(summary = "비밀번호 수정", description = "비밀번호 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = """
                                                            패스워드 수정 실패
                                                            """
            )
    })
    @PatchMapping("/{patientSeq}/password")
    public ResponseEntity changePassword(@PathVariable Long patientSeq,
                                         @PathVariable(name = "category", value = "category") String category,
                                         @RequestBody PatientModifyPasswordReq request) {
        patientService.changePassword(patientSeq, request);

        return ResponseEntity.ok().build();
    }

    @ResponseBody
    @Operation(summary = "비밀번호 초기화", description = "비밀번호 초기화")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = """
                                                            패스워드 초기화 실패
                                                            """
            )
    })
    @PatchMapping("/{patientSeq}/password/reset")
    public ResponseEntity resetPassword(@PathVariable(name = "category", value = "category") String category,
                                        @PathVariable Long patientSeq
                                        ) {

        patientService.resetPassword(patientSeq);

        return ResponseEntity.ok().build();
    }

    @ResponseBody
    @Operation(summary = "앱스토어 설치 링크 문자 발송", description = "앱스토어 설치 링크 문자 발송(앱스토어 배포 후 SmsUtil 내 링크 수정 필요)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "500", description = """
                                                             responseStatus.code
                                                             \n9003 -> 앱스토어 설치 링크 문자 발송 실패 
                                                             """)
    })
    @GetMapping("/{patientSeq}/sms")
    public ResponseEntity sendSmsForAppStoreLink(@PathVariable(name = "category", value = "category") String category,
                                                 @PathVariable Long patientSeq) {

        patientService.sendSmsForAppStoreLink(patientSeq);

        return ResponseEntity.ok().build();
    }

}
