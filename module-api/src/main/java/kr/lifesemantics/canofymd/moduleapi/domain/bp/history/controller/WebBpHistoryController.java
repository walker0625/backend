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
import kr.lifesemantics.canofymd.moduleapi.domain.bp.enums.Unit;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.req.BpHistoryRegisterReq;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.res.BpHistoryLatestRes;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.res.BpHistoryRegisterRes;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.service.BpHistoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@SecurityRequirement(name = "Bearer Authentication")
@SecurityScheme( name = "Bearer Authentication", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "Bearer")
@RestController
@RequestMapping("/bpai/web/bp-history")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequiredArgsConstructor
@Tag(name = "[BPAI]혈압기록 정보 API(WEB 용)")
public class WebBpHistoryController {

    BpHistoryService bpHistoryService;

    @GetMapping("/patients/{patientSeq}/date")
    @Operation(summary = "특정 환자의 일간별 혈압 목록 조회", description = "특정 환자의 일간별 혈압 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = BpHistoryLatestRes.class))}),
            @ApiResponse(responseCode = "404", description = """
                                                            responseStatus.code
                                                            \n5001 -> 혈압 기록이 없음
                                                            """)
    })
    public ResponseEntity date(@PathVariable Long patientSeq) {
        return ResponseEntity.ok().body(bpHistoryService.dates(patientSeq));
    }

    @GetMapping("/patients/{patientSeq}/period")
    @Operation(summary = "특정 환자의 기간내(from, to) 일별 혈압 평균 조회", description = "특정 환자의 기간내(from, to) 일별 혈압 평균 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = """
                                                            responseStatus.code
                                                            \n5001 -> 혈압 기록이 없음
                                                            """)
    })
    public ResponseEntity period(@PathVariable Long patientSeq,
                                 @RequestParam @DateTimeFormat(pattern = "YYYY-mm-dd") LocalDate from,
                                 @RequestParam @DateTimeFormat(pattern = "YYYY-mm-dd") LocalDate to) {
        return ResponseEntity.ok().body(bpHistoryService.period(patientSeq, from, to));
    }

    @GetMapping("/patients/{patientSeq}/statistics")
    @Operation(summary = "특정 환자의 주차 별 통계 데이터", description = "특정 환자의 주차 별 통계 데이터")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = """
                                                            responseStatus.code
                                                            \n5001 -> 혈압 기록이 없음
                                                            """)
    })
    public ResponseEntity statistics(@PathVariable Long patientSeq, Unit unit) {
        return ResponseEntity.ok().body(bpHistoryService.statistics(patientSeq, unit));
    }


    @GetMapping("/patients/{patientSeq}/averages")
    @Operation(summary = "특정 환자의 특정 기간 별 평균", description = "특정 환자의 특정 기간 별 평균")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "404", description = """
                                                            responseStatus.code
                                                            \n5001 -> 혈압 기록이 없음
                                                            """)
    })
    public ResponseEntity averages(@PathVariable Long patientSeq) {
        return ResponseEntity.ok().body(bpHistoryService.report(patientSeq));
    }

}