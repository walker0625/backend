package kr.lifesemantics.canofymd.moduleapi.domain.bp.history.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.req.BpHistoryDeleteReq;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.req.BpHistoryModifyReq;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.req.BpHistoryRegisterReq;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.res.BpHistoryDetailRes;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.res.BpHistoryLastWeekRes;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.res.BpHistoryLatestRes;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.res.BpHistoryRegisterRes;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.service.BpHistoryService;
import kr.lifesemantics.canofymd.moduleapi.global.config.security.SecurityUserDetails;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@SecurityRequirement(name = "Bearer Authentication")
@SecurityScheme( name = "Bearer Authentication", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "Bearer")
@RestController
@RequestMapping("/bpai/app/bp-history")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequiredArgsConstructor
@Tag(name = "[BPAI]혈압기록 정보 API(APP 용)")
public class AppBpHistoryController {

    BpHistoryService bpHistoryService;

    @PostMapping
    @Operation(summary = "혈압 등록", description = "혈압 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = BpHistoryRegisterRes.class))}),
            @ApiResponse(responseCode = "404", description = """
                                                            responseStatus.code
                                                            \n4002 -> 해당 환자가 없음
                                                            """
            )
    })
    public ResponseEntity register(@AuthenticationPrincipal SecurityUserDetails details,
                                   @RequestBody BpHistoryRegisterReq request) {

        request.setPatientSeq(details.getSeq());

        return ResponseEntity.ok().body(bpHistoryService.register(request));
    }

    @PatchMapping
    @Operation(summary = "혈압 수정", description = "혈압 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = """
                                                            responseStatus.code
                                                            \5001 -> 해당 혈압 기록이 없음
                                                            """
            )
    })
    public ResponseEntity modify(@RequestBody BpHistoryModifyReq request) {

        bpHistoryService.modify(request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @Operation(summary = "혈압 삭제", description = "혈압 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = """
                                                            responseStatus.code
                                                            \5001 -> 해당 혈압 기록이 없음
                                                            """
            )
    })
    public ResponseEntity delete(@RequestBody BpHistoryDeleteReq request) {

        bpHistoryService.delete(request.getSeq());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{bpHistorySeq}")
    @Operation(summary = "혈압 상세 조회", description = "혈압 상세 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = BpHistoryDetailRes.class))}),
            @ApiResponse(responseCode = "404", description = """
                                                            responseStatus.code
                                                            \n5001 -> 혈압 기록이 없음
                                                            """)
    })
    public ResponseEntity detail(@PathVariable Long bpHistorySeq) {
        return ResponseEntity.ok().body(bpHistoryService.detail(bpHistorySeq));
    }

    @GetMapping("/latest")
    @Operation(summary = "최근 혈압 조회", description = "환자의 최근 혈압 1개 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = BpHistoryLatestRes.class))}),
            @ApiResponse(responseCode = "404", description = """
                                                            responseStatus.code
                                                            \n5001 -> 혈압 기록이 없음
                                                            """)
    })
    public ResponseEntity latest(@AuthenticationPrincipal SecurityUserDetails details) {
        return ResponseEntity.ok().body(bpHistoryService.latest(details.getSeq()));
    }

    @GetMapping("/last-week")
    @Operation(summary = "지난주 최고/최저 혈압", description = "지난주 최고/최저 혈압(수축기 기준)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = BpHistoryLastWeekRes.class))}),
            @ApiResponse(responseCode = "404", description = """
                                                            responseStatus.code
                                                            \n4002 -> 해당 환자가 없음
                                                            """)
    })
    public ResponseEntity lastWeek(@AuthenticationPrincipal SecurityUserDetails details) {
        return ResponseEntity.ok().body(bpHistoryService.lastWeek(details.getSeq()));
    }

    @GetMapping("/dates")
    @Operation(summary = "일간별 혈압 목록 조회", description = "일간별 혈압 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    public ResponseEntity dates(@AuthenticationPrincipal SecurityUserDetails details) {
        return ResponseEntity.ok().body(bpHistoryService.dates(details.getSeq()));
    }

    @GetMapping("/continuity")
    @Operation(summary = "연속 측정 성공주 횟수(뱃지) 및 실패 주 존재 유무", description = "(오늘 - 55) ~ 오늘 = 8주 까지 혈압 기록 중 연속 측정 성공주 횟수와 실패 주 유무(안내 팝업용)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    public ResponseEntity continuity(@AuthenticationPrincipal SecurityUserDetails details) {
        return ResponseEntity.ok().body(bpHistoryService.continuity(details.getSeq()));
    }

    @GetMapping("/period")
    @Operation(summary = "기간내(from, to) 일별 혈압 평균 조회(그래프) 및 주간 최고/최저/평균 혈압(통계 정보)", description = "기간내(from, to) 일별 혈압 평균 조회(그래프) 및 주간 최고/최저/평균 혈압(통계 정보)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공")
    })
    public ResponseEntity period(@AuthenticationPrincipal SecurityUserDetails details,
                                 @RequestParam @DateTimeFormat(pattern = "YYYY-mm-dd") LocalDate from,
                                 @RequestParam @DateTimeFormat(pattern = "YYYY-mm-dd") LocalDate to) {
        return ResponseEntity.ok().body(bpHistoryService.period(details.getSeq(), from, to));
    }

}