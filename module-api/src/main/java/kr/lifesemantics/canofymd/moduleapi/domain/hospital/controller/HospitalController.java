package kr.lifesemantics.canofymd.moduleapi.domain.hospital.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.lifesemantics.canofymd.moduleapi.domain.hospital.dto.req.HospitalModifyReq;
import kr.lifesemantics.canofymd.moduleapi.domain.hospital.dto.req.HospitalRegisterReq;
import kr.lifesemantics.canofymd.moduleapi.domain.hospital.dto.res.HospitalDetailRes;
import kr.lifesemantics.canofymd.moduleapi.domain.hospital.dto.res.HospitalFilterRes;
import kr.lifesemantics.canofymd.moduleapi.domain.hospital.dto.res.HospitalFindListRes;
import kr.lifesemantics.canofymd.moduleapi.domain.hospital.mapper.HospitalMapper;
import kr.lifesemantics.canofymd.moduleapi.domain.hospital.service.HospitalService;
import kr.lifesemantics.canofymd.moduleapi.global.config.security.SecurityUserDetails;
import kr.lifesemantics.canofymd.modulecore.util.ResponseStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "Bearer Authentication")
@SecurityScheme( name = "Bearer Authentication", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "Bearer")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/hospitals")
@Tag(name = "병원 정보 API", description = "병원 관리 목적의 API Docs")
public class HospitalController {

    HospitalService hospitalService;
    HospitalMapper hospitalMapper;

    @PostMapping
    @ResponseBody
    @Operation(summary = "병원 등록", description = "병원 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = HospitalFindListRes.class))}),
            @ApiResponse(responseCode = "400", description = """
                                                            responseStatus.code
                                                            \n2003 -> 병원 생성 시 문제 발생
                                                            """
            )
    })
    public ResponseEntity create(@RequestBody HospitalRegisterReq request) {
        return ResponseEntity.ok().body(hospitalService.create(request));
    }

    @Operation(summary = "병원 목록", description = "전체 병원 목록 조회 - 관리자 사용(검색 키워드 : 이름)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = HospitalFindListRes.class))})
    })
    @GetMapping
    public ResponseEntity list(@RequestParam(required = false) String keyword,
                               @PageableDefault(size = 10) Pageable pageable) {

        return ResponseEntity.ok().body(hospitalService.findHospitalsByName(keyword, pageable));
    }

    @Operation(summary = "병원 필터용 목록", description = "ADMIN은 모든 활성화된 병원을, 그 외는 활성화된 소속 병원만 조회 가능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = HospitalFilterRes.class))})
    })
    @GetMapping("/filter")
    public ResponseEntity filter(@AuthenticationPrincipal SecurityUserDetails securityUserDetails) {
        return ResponseEntity.ok().body(hospitalService.filter(securityUserDetails.getUserType(), securityUserDetails.getHospitalSeq()));
    }

    @Operation(summary = "병원 중복 ID 체크", description = "중복일 경우 false 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    @GetMapping("/duplicate")
    public ResponseEntity nameChecks(String name) {
        return ResponseEntity.ok().body(hospitalService.isDuplicatedName(name));
    }

    @PatchMapping
    @Operation(summary = "병원 수정", description = "등록된 병원의 정보 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = """
                                                            responseStatus.code
                                                            \n2001 -> 병원을 찾을 수 없는 경우
                                                            """)
    })
    public ResponseEntity modify(@RequestBody HospitalModifyReq hospitalModifyReq) {

        hospitalService.modify(hospitalModifyReq);

        return ResponseEntity.ok().body(ResponseStatus.SUCCESS);
    }

    @Operation(summary = "병원 상세", description = "병원 1개의 상세 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = HospitalDetailRes.class))}),
            @ApiResponse(responseCode = "404", description = """
                                                            responseStatus.code
                                                            \n2001 -> 병원을 찾을 수 없는 경우
                                                            """)
    })
    @GetMapping("/{hospitalSeq}")
    public ResponseEntity findById(@PathVariable Long hospitalSeq) {
        return ResponseEntity.ok().body(hospitalMapper.toHospitalDetailRes(hospitalService.findById(hospitalSeq)));
    }

}