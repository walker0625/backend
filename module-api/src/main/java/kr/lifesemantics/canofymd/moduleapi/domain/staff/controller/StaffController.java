package kr.lifesemantics.canofymd.moduleapi.domain.staff.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.lifesemantics.canofymd.moduleapi.domain.staff.dto.req.StaffDeleteReq;
import kr.lifesemantics.canofymd.moduleapi.domain.staff.dto.req.StaffModifyReq;
import kr.lifesemantics.canofymd.moduleapi.domain.staff.dto.req.StaffPasswordModifyReq;
import kr.lifesemantics.canofymd.moduleapi.domain.staff.dto.req.StaffRegisterReq;
import kr.lifesemantics.canofymd.moduleapi.domain.staff.dto.res.StaffDetailRes;
import kr.lifesemantics.canofymd.moduleapi.domain.staff.dto.res.StaffListRes;
import kr.lifesemantics.canofymd.moduleapi.domain.staff.mapper.StaffMapper;
import kr.lifesemantics.canofymd.moduleapi.domain.staff.service.StaffService;
import kr.lifesemantics.canofymd.modulecore.util.ResponseStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "Bearer Authentication")
@SecurityScheme( name = "Bearer Authentication", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "Bearer")
@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "병원관계자 정보 API", description = "병원관계자 관리 목적의 API Docs")
@RequestMapping("/staffs")
public class StaffController {

    StaffService staffService;
    StaffMapper staffMapper;

    @Operation(summary = "병원관계자 등록", description = "병원관계자 정보 등록 후 등록한 병원관계자 정보 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = """
                                                             responseStatus.code
                                                             \n2001 -> 병원을 찾을 수 없는 경우
                                                             """
            ),
            @ApiResponse(responseCode = "500", description = """
                                                             responseStatus.code
                                                             \n5002 -> 병원관계자 정보 DB 저장 중에 실패한 경우
                                                             """)
    })
    @PostMapping
    public ResponseEntity register(@io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
            schema = @Schema(implementation = StaffRegisterReq.class),
            examples = {
                    @ExampleObject(
                    name = "doctor sample", value = """
                    {
                      "id": "doctor2",
                      "name": "김의사",
                      "userType": "DOCTOR",
                      "contact": "010-1111-1111",
                      "hospitalSeq": 1,
                      "category": [
                        "BPAI"
                      ]
                    }
                    """),
                    @ExampleObject(
                    name = "coordinator sample", value = """
                    {
                      "id": "coordinator2",
                      "name": "김코디",
                      "userType": "COORDINATOR",
                      "contact": "010-1111-1111",
                      "hospitalSeq": 1,
                      "category": [
                        "BPAI"
                      ]
                    }
                    """),

            }

    ))
                                       @RequestBody StaffRegisterReq staffRegisterReq) {
        return ResponseEntity.ok().body(staffService.register(staffRegisterReq));
    }

    @Operation(summary = "병원관계자 목록", description = "병원관계자 목록 페이징 및 검색")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = StaffListRes.class))})
    })
    @GetMapping("/hospitals/{hospitalSeq}")
    public ResponseEntity list(@PathVariable Long hospitalSeq,
                               @RequestParam(required = false) String keyword,
                               @PageableDefault(size = 10) Pageable pageable) {

        return ResponseEntity.ok().body(staffService.list(hospitalSeq, keyword, pageable));
    }

    @Operation(summary = "병원관계자 수정", description = "병원관계자 정보를 수정한다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    @PatchMapping
    public ResponseEntity modify(@RequestBody StaffModifyReq staffModifyReq) {

        staffService.modify(staffModifyReq);

        return ResponseEntity.ok().body(ResponseStatus.SUCCESS);
    }

    @Operation(summary = "병원관계자 중복 ID 체크", description = "중복일 경우 true 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    @GetMapping("/duplicate")
    public ResponseEntity isDuplicatedId(String id) {
        return ResponseEntity.ok().body(staffService.isDuplicatedId(id));
    }

    @Operation(summary = "병원관계자 상세", description = "병원관계자 한명의 정보를 불러온다")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = StaffDetailRes.class))}),
                @ApiResponse(responseCode = "404", description = """
                                                                 responseStatus.code
                                                                 \n5001 -> 병원관계자를 찾을 수 없는 경우
                                                                 """
                )
    })
    @GetMapping("/{staffSeq}")
    public ResponseEntity findById(@PathVariable Long staffSeq) {
        return ResponseEntity.ok().body(staffMapper.toStaffDetailRes(staffService.findById(staffSeq)));
    }

    @Operation(summary = "병원관계자 비밀번호 수정", description = "병원관계자 비밀번호를 수정한다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    @PatchMapping("/password")
    public ResponseEntity modify(@RequestBody StaffPasswordModifyReq staffPasswordModifyReq) {

        staffService.passwordModify(staffPasswordModifyReq);

        return ResponseEntity.ok().body(ResponseStatus.SUCCESS);
    }

    /*@Operation(summary = "병원관계자 복수 삭제", description = "병원 관계자들을 삭제한다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    @DeleteMapping
    public ResponseEntity delete(@RequestBody StaffDeleteReq request) {

//        staffService.deleteAll(staffPasswordModifyReq);

        return ResponseEntity.ok().body(ResponseStatus.SUCCESS);
    }
*/
}