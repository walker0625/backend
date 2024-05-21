
package kr.lifesemantics.canofymd.moduleapi.domain.dashboard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.lifesemantics.canofymd.moduleapi.domain.dashboard.dto.res.DashboardPeriodRes;
import kr.lifesemantics.canofymd.moduleapi.domain.dashboard.dto.res.DashboardStatisticsRes;
import kr.lifesemantics.canofymd.moduleapi.domain.dashboard.service.DashboardService;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.service.PatientService;
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
@RequestMapping("/bpai/web/dashboard")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequiredArgsConstructor
@Tag(name = "[BPAI]대시보드 정보 API(WEB 용)")
public class DashboardController {

    DashboardService dashboardService;

    @GetMapping("/statistics")
    @Operation(summary = "가입, 어제/오늘 측정자, 누적 혈압 예측자", description = "WEB 대시보드 상단 통계 정보")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = DashboardStatisticsRes.class))})
    })
    public ResponseEntity statistics(@AuthenticationPrincipal SecurityUserDetails userDetails) {
        return ResponseEntity.ok().body(dashboardService.statistics(userDetails.getHospitalSeq()));
    }

    @GetMapping("/period")
    @Operation(summary = "기간내 일별 측정자 수", description = "WEB 대시보드 우하단 일별 측정자 수 그래프")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = DashboardPeriodRes.class))})
    })
    public ResponseEntity period(@AuthenticationPrincipal SecurityUserDetails securityUserDetails,
                                 @RequestParam @DateTimeFormat(pattern = "YYYY-mm-dd") LocalDate from,
                                 @RequestParam @DateTimeFormat(pattern = "YYYY-mm-dd") LocalDate to) {
        return ResponseEntity.ok().body(dashboardService.period(securityUserDetails.getHospitalSeq(), from, to));
    }

}