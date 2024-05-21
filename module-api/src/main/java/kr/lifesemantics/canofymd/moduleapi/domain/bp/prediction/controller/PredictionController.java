package kr.lifesemantics.canofymd.moduleapi.domain.bp.prediction.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.service.BpHistoryService;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.prediction.dto.res.PredictionDetailRes;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.prediction.service.PredictionService;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.summary.dto.res.BpPredictableRes;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.res.PatientFindListRes;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.bp.prediction.controller
 * fileName       : PredictionController
 * author         : ms.jo
 * date           : 2024/05/02
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/05/02        ms.jo       최초 생성
 */

@SecurityRequirement(name = "Bearer Authentication")
@SecurityScheme( name = "Bearer Authentication", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "Bearer")
@RestController
@RequestMapping("/predictions")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "[BPAI]예측 정보 API")
public class PredictionController {

    PredictionService predictionService;

//    @Operation(summary = "예측 가능 여부 조회", description = "예측 가능 여부 조회")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = PatientFindListRes.class))})
//    })
//    @GetMapping("/patient/{patientSeq}/check")
//    public ResponseEntity isPredictable(@PathVariable Long patientSeq) {
//        return ResponseEntity.ok(predictionService.checkOnModel(patientSeq));
//    }

    @Operation(summary = "예측 가능 여부 및 참여율 조회", description = "예측 가능 여부 및 참여율")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = BpPredictableRes.class))})
    })
    @GetMapping("/patients/{patientSeq}/check")
    public ResponseEntity getPredictableInfo(@PathVariable Long patientSeq) {

        BpPredictableRes result = predictionService.findIsPredictableAndRate(patientSeq);

        return ResponseEntity.ok(result);
    }


    @Operation(summary = "예측 요청", description = "예측 요청")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = {@Content(schema = @Schema(implementation = PredictionDetailRes.class))})
    })
    @GetMapping("/patient/{patientSeq}")
    public ResponseEntity predictRequest(@PathVariable Long patientSeq) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(predictionService.predict(patientSeq));
    }

}
