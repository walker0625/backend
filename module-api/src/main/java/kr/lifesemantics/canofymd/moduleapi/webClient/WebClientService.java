package kr.lifesemantics.canofymd.moduleapi.webClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.dto.req.DiagnoseAiReq;
import kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.dto.res.DiagnoseAiRes;
import kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.dto.res.DiagnoseResultRes;
import kr.lifesemantics.canofymd.moduleapi.webClient.dto.req.PredictionHolderReq;
import kr.lifesemantics.canofymd.moduleapi.webClient.dto.res.PredictionCheckRes;
import kr.lifesemantics.canofymd.moduleapi.webClient.dto.res.PredictionRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.webClient
 * fileName       : WebClientService
 * author         : ms.jo
 * date           : 2024/05/03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/05/03        ms.jo       최초 생성
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class WebClientService {

    private final WebClient webClient;

    @Value("${bpai.prediction.server.url}")
   	private String PREDICTION_SERVER_URL;

    @Value("${scai.diagnose.api.url}")
    private String SCAI_DIAGNOSE_API_URL;

   	@Value("${spring.profiles.active:}")
   	private String ACTIVE_PROFILES;

    @Autowired
    private ObjectMapper objectMapper;

    public Mono<PredictionRes> predict(PredictionHolderReq predictionReq) {

        String body;

        try {
            body = objectMapper.writeValueAsString(predictionReq);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Mono<PredictionRes> predictionResMono = webClient
                .post()
                .uri(PREDICTION_SERVER_URL + "prediction/bloodpressure")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(PredictionRes.class);


        return predictionResMono;

    }

    public Mono<PredictionCheckRes> checkPredictable(PredictionHolderReq predictionReq) {
//    public Mono<Map> checkPredictable(PredictionHolderReq predictionReq) {

        String body;

        try {
            body = objectMapper.writeValueAsString(predictionReq);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        return webClient
                .post()
                .uri(PREDICTION_SERVER_URL + "resource/bloodpressure/check")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(PredictionCheckRes.class);
//                .bodyToMono(Map.class);

    }

    public Mono<DiagnoseAiRes> diagnose(DiagnoseAiReq diagnoseAiReq){

        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        try {
            builder.part("file", new ByteArrayResource(diagnoseAiReq.getFile().getBytes()) {
                @Override
                public String getFilename() {
                    return diagnoseAiReq.getFile().getOriginalFilename();
                }
            });
        }catch (Exception e) {
            log.info("e {}", e);
        }

        builder.part("lesionsite", diagnoseAiReq.getLesionSite());

        return webClient.mutate()
                .build()
                .post()
                .uri(SCAI_DIAGNOSE_API_URL)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(builder.build())
                .retrieve().bodyToMono(DiagnoseAiRes.class);
    }

}
