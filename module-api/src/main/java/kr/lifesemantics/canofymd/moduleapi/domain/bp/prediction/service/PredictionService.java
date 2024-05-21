package kr.lifesemantics.canofymd.moduleapi.domain.bp.prediction.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.res.ContinuityWeekRes;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.service.BpHistoryService;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.vo.BpSummary;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.prediction.dto.res.BP;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.prediction.dto.res.PredictionDetailRes;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.prediction.repository.PredicationRepository;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.summary.dto.res.BpPredictableRes;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.service.PatientService;
import kr.lifesemantics.canofymd.moduleapi.global.util.StatisticsUtil;
import kr.lifesemantics.canofymd.moduleapi.webClient.WebClientService;
import kr.lifesemantics.canofymd.moduleapi.webClient.dto.req.PredictionHolderReq;
import kr.lifesemantics.canofymd.moduleapi.webClient.dto.req.PredictionReq;
import kr.lifesemantics.canofymd.moduleapi.webClient.dto.res.PredictionAmount;
import kr.lifesemantics.canofymd.moduleapi.webClient.dto.res.PredictionCheckRes;
import kr.lifesemantics.canofymd.moduleapi.webClient.dto.res.PredictionRes;
import kr.lifesemantics.canofymd.modulecore.domain.bp.BpHistory;
import kr.lifesemantics.canofymd.modulecore.domain.bp.predication.Predication;
import kr.lifesemantics.canofymd.modulecore.domain.bp.value.SystolicDiastolic;
import kr.lifesemantics.canofymd.modulecore.domain.user.Patient;
import kr.lifesemantics.canofymd.modulecore.domain.bp.value.BloodPressure;
import kr.lifesemantics.canofymd.modulecore.exception.BusinessException;
import kr.lifesemantics.canofymd.modulecore.util.ResponseStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.bp.prediction.service
 * fileName       : PredictionService
 * author         : ms.jo
 * date           : 2024/05/02
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/05/02        ms.jo       최초 생성
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PredictionService {

    PatientService patientService;
    WebClientService webClientService;
    BpHistoryService bpHistoryService;
    PredicationRepository predicationRepository;

    public PredictionDetailRes predict(Long patientSeq) {

        PredictionHolderReq request = mappingForPrediction(patientSeq);

        //prediction server req
        Mono<PredictionRes> predict = webClientService.predict(request);

        PredictionRes predictionRes = Optional.ofNullable(predict.block()).orElse(PredictionRes.createEmptyObject());

        if(predictionRes.getResponseStatus().getCode() == 1000) {

            PredictionAmount response = predictionRes.getEntity();
            List<Double> predictedSystolic = response.getPredictedSystolic();
            List<Double> predictedDiastolic = response.getPredictedDiastolic();

            PredictionDetailRes result = new PredictionDetailRes();

            IntStream.range(0, 4).forEach(i -> {
                switch (i) {
                    case 0 -> result.setWeek1(new BP(predictedSystolic.get(i), predictedDiastolic.get(i)));
                    case 1 -> result.setWeek2(new BP(predictedSystolic.get(i), predictedDiastolic.get(i)));
                    case 2 -> result.setWeek3(new BP(predictedSystolic.get(i), predictedDiastolic.get(i)));
                    case 3 -> result.setWeek4(new BP(predictedSystolic.get(i), predictedDiastolic.get(i)));
                }
            });
            predicationRepository.save(
                    Predication.create(
                            patientService.findById(patientSeq),
                            new SystolicDiastolic(result.getWeek1().getSystolic(), result.getWeek1().getDiastolic()),
                            new SystolicDiastolic(result.getWeek2().getSystolic(), result.getWeek2().getDiastolic()),
                            new SystolicDiastolic(result.getWeek3().getSystolic(), result.getWeek3().getDiastolic()),
                            new SystolicDiastolic(result.getWeek4().getSystolic(), result.getWeek4().getDiastolic())));

            return result;
        }
        else {
            throw new BusinessException(ResponseStatus.CAN_NOT_PREDICT, HttpStatus.BAD_REQUEST);
        }

    }

    private PredictionHolderReq mappingForPrediction(Long patientSeq) {
        Patient patient = patientService.findById(patientSeq);

        LocalDateTime from = bpHistoryService.getStandardFrom(patient);

        LocalDateTime to = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        List<Double> sbp = new ArrayList<>();
        List<Double> dbp = new ArrayList<>();
        List<Double> pulse = new ArrayList<>();
        List<String> date = new ArrayList<>();

        List<BpHistory> histories = bpHistoryService.getByPatientSeqAndIsUsableIsTrueAndMeasureTimeBetween(patientSeq, from, to);

        parseData(histories, sbp, dbp, pulse, date);

        PredictionHolderReq request = new PredictionHolderReq(new PredictionReq(to.toLocalDate().toString(), sbp, dbp, pulse, date));
        return request;
    }

    public BpPredictableRes findIsPredictableAndRate(Long patientSeq) {

        Patient patient = patientService.findById(patientSeq);
        ContinuityWeekRes continuity = bpHistoryService.continuity(patientSeq);

        double measureRate = 0;
        int measuredCountInWeek;
        List<BpHistory> histories;
        List<BpSummary> summaries;

        //for measuredCountInWeek
        LocalDateTime from = LocalDateTime.of(LocalDate.now().minusDays(7), LocalTime.MIN);
        LocalDateTime to = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59, 9999));

        histories = bpHistoryService.getByPatientSeqAndIsUsableIsTrueAndMeasureTimeBetween(patientSeq, from, to);
        summaries = bpHistoryService.calculateAverage(histories);

        measuredCountInWeek = summaries.isEmpty() ? 0 : StatisticsUtil.getMeasureRate(summaries.get(summaries.size()-1));

        //for measureRate
        if(patient.getFirstMeasuredDate() != null) {
            from = bpHistoryService.getStandardFrom(patient);

            histories = bpHistoryService.getByPatientSeqAndIsUsableIsTrueAndMeasureTimeBetween(patientSeq, from, to);
            summaries = bpHistoryService.calculateAverage(histories);

            measureRate = StatisticsUtil.getParticipantRate(summaries, (int) Duration.between(from.toLocalDate().atStartOfDay(), to.toLocalDate().atStartOfDay()).toDays());
        }

//        PredictionHolderReq predictionHolderReq = mappingForPrediction(patientSeq);
//        PredictionCheckRes response = webClientService.checkPredictable(predictionHolderReq).block();

        return new BpPredictableRes(
//                response.getResponseStatus().getCode() == 1000 ? response.getResult() : false,
                continuity.getContinuityCount() >= 8,
                measureRate,
                measuredCountInWeek);

    }


    protected void parseData(List<BpHistory> histories, List<Double> sbp, List<Double> dbp, List<Double> pulse, List<String> date) {
        histories.forEach(i -> {
            List<BloodPressure> bp = List.of(i.getFirstBP(), i.getSecondBP());
            sbp.addAll(bp.stream().mapToDouble(BloodPressure::getSystolic).boxed().toList());
            dbp.addAll(bp.stream().mapToDouble(BloodPressure::getDiastolic).boxed().toList());
            pulse.addAll(bp.stream().mapToDouble(BloodPressure::getPulse).boxed().toList());
            date.addAll(List.of(i.getMeasureTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString(), i.getMeasureTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString()));
        });
    }

}
