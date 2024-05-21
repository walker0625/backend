package kr.lifesemantics.canofymd.moduleapi.domain.bp.history.service;

import kr.lifesemantics.canofymd.moduleapi.domain.bp.enums.Unit;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.req.BpHistoryModifyReq;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.req.BpHistoryRegisterReq;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.res.*;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.mapper.BpHistoryMapper;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.repository.BpHistoryRepository;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.vo.PeriodVO;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.statistics.vo.BPReachRateVO;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.statistics.vo.BPSummaryVO;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.service.PatientService;
import kr.lifesemantics.canofymd.moduleapi.domain.treatment.service.TreatmentService;
import kr.lifesemantics.canofymd.moduleapi.global.util.StatisticsUtil;
import kr.lifesemantics.canofymd.modulecore.domain.bp.BpHistory;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.vo.BpSummary;

import kr.lifesemantics.canofymd.modulecore.domain.bp.Treatment;
import kr.lifesemantics.canofymd.modulecore.domain.bp.value.BloodPressure;
import kr.lifesemantics.canofymd.modulecore.domain.user.Patient;
import kr.lifesemantics.canofymd.modulecore.enums.ParticipationCompliance;
import kr.lifesemantics.canofymd.modulecore.exception.BusinessException;
import kr.lifesemantics.canofymd.modulecore.util.ResponseStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static kr.lifesemantics.canofymd.moduleapi.global.util.StatisticsUtil.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BpHistoryService {

    PatientService patientService;
    TreatmentService treatmentService;
    BpHistoryRepository bpHistoryRepository;
    BpHistoryMapper bpHistoryMapper;

    LocalDateTime NOW_LOCAL_DATE_TIME_FOR_NOW = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59, 9999));

    @Transactional
    public BpHistoryRegisterRes register(BpHistoryRegisterReq request) {

        request.setFirstBP(judgeBPStatus(request.getFirstBP()));
        request.setSecondBP(judgeBPStatus(request.getSecondBP()));

        Patient patient = patientService.findById(request.getPatientSeq());
        LocalDate firstMeasuredDate = patient.getFirstMeasuredDate();
        LocalDate lastMeasuredDate = patient.getLastMeasuredDate();

        LocalDate measureDate = request.getMeasureTime().toLocalDate();

        int measureHour = request.getMeasureTime().getHour();

        // 유효 측정 시간대 AM 4 ~ PM 12
        if(measureHour >= 4 && measureHour <= 12) {
            request.setUsable(true);
        }

        int week = StatisticsUtil.judgeWeek(
                firstMeasuredDate == null ? request.getMeasureTime().toLocalDate() : firstMeasuredDate,
                measureDate
        );

        BpHistory bpHistory = BpHistory.create(patient, request.getFirstBP(), request.getSecondBP(), week, request.getMeasureTime(), request.isUsable());

        BpHistory savedHistory = bpHistoryRepository.save(bpHistory);

        syncPatientMeasuredInfo(patient);

        return bpHistoryMapper.toBpHistoryRegisterRes(savedHistory);
    }

    @Transactional
    public void updateMeasureStatus(Patient patient) {

        LocalDateTime from = getStandardFrom(patient);
        LocalDateTime to = NOW_LOCAL_DATE_TIME_FOR_NOW;

        List<BpHistory> histories = getByPatientSeqAndIsUsableIsTrueAndMeasureTimeBetween(patient.getSeq(), from, to);
        List<BpSummary> summaries = calculateAverage(histories);

        ParticipationCompliance compliance = StatisticsUtil.getParticipationCompliance(summaries, (int) Duration.between(from.toLocalDate().atStartOfDay(), to.toLocalDate().atStartOfDay()).toDays());

        patient.changeMeasureStatus(compliance);

    }

    @Transactional(readOnly = true)
    public BpHistoryDetailRes detail(Long bpHistorySeq) {

        BpHistory bpHistory= bpHistoryRepository.findById(bpHistorySeq).orElseThrow(() ->
                new BusinessException(ResponseStatus.NOT_EXIST_BP, HttpStatus.NOT_FOUND)
        );

        return bpHistoryMapper.toBpHistoryDetailRes(bpHistory);
    }

    @Transactional(readOnly = true)
    public BpHistoryLatestRes latest(Long patientSeq) {

        BpHistory latestBp = bpHistoryRepository.findFirstByPatientSeqOrderByMeasureTimeDesc(patientSeq);

        if(latestBp == null) {
            throw new BusinessException(ResponseStatus.NOT_EXIST_BP, HttpStatus.NOT_FOUND);
        }

        return bpHistoryMapper.toBpHistoryLatestRes(latestBp);
    }

    @Transactional(readOnly = true)
    public BpHistoryLastWeekRes lastWeek(Long patientSeq) {

        int lastWeek = StatisticsUtil.judgeWeek(patientService.findById(patientSeq).getFirstMeasuredDate(), LocalDate.now()) - 1;

        if (lastWeek < 1) { // 첫주의 경우
            return new BpHistoryLastWeekRes(new BloodPressure(0,0,0), new BloodPressure(0,0,0));
        }

        List<BpHistory> lastWeekBpList = bpHistoryRepository.findByPatientSeqAndWeek(patientSeq, lastWeek);

        List<BloodPressure> bloodPressureList = lastWeekBpList.stream()
                .flatMap(bpHistory -> Stream.of(bpHistory.getFirstBP(), bpHistory.getSecondBP()))
                .toList();

        BloodPressure maxSystolicBloodPressure = bloodPressureList.stream()
                .max(Comparator.comparingInt(BloodPressure::getSystolic)).orElse(new BloodPressure(0,0,0));

        BloodPressure minSystolicBloodPressure = bloodPressureList.stream()
                .min(Comparator.comparingInt(BloodPressure::getSystolic)).orElse(new BloodPressure(0,0,0));

        return new BpHistoryLastWeekRes(maxSystolicBloodPressure, minSystolicBloodPressure);
    }

    @Transactional(readOnly = true)
    public Map<String, List<BpHistoryDetailRes>> dates(Long patientSeq) {

        List<BpHistory> bpHistoryList = bpHistoryRepository.findByPatientSeqOrderByMeasureTimeDesc(patientSeq);

        Map<String, List<BpHistoryDetailRes>> entityMap = new HashMap<>();
        entityMap.put("entity", bpHistoryList.stream().map(bpHistoryMapper::toBpHistoryDetailRes).toList());

        return entityMap;
    }

    @Transactional(readOnly = true)
    public WeekBpRes period(Long patientSeq, LocalDate from, LocalDate to) {

        LocalDateTime fromDateTime = LocalDateTime.of(from, LocalTime.MIN);
        // LocalTime.MAX로 DB에 쿼리하면 다음날까지 조회되는 이슈 있음
        LocalDateTime toDateTime = LocalDateTime.of(to, LocalTime.of(23, 59, 59, 9999));

        List<BpHistory> bpHistoryList = bpHistoryRepository.findByPatientSeqAndMeasureTimeBetween(patientSeq, fromDateTime, toDateTime);

        Map<String, List<BloodPressure>> dateBpMap = bpHistoryList.stream().collect(
                Collectors.groupingBy(
                        bp -> bp.getMeasureTime().toLocalDate().toString(),
                        Collectors.collectingAndThen(
                                toList(),
                                bpHistories -> bpHistories.stream().flatMap(
                                        bpHistory -> Stream.of(bpHistory.getFirstBP(), bpHistory.getSecondBP())
                                ).collect(toList())
                        )
                )
        );

        List<BpHistoryGraphRes> graphBpList = dateBpMap.entrySet().stream()
                .sorted(Map.Entry.<String, List<BloodPressure>>comparingByKey().reversed())
                .map(entry -> new BpHistoryGraphRes(
                        entry.getKey(),
                        StatisticsUtil.calculateAverage(entry.getValue())
                ))
                .collect(toList());

        List<BloodPressure> weekBpList = dateBpMap.values().stream().flatMap(List::stream).toList();

        BloodPressure maxBp  = judgeBPStatus(weekBpList.stream().max(Comparator.comparingInt(BloodPressure::getSystolic))
                .orElseGet(() ->new BloodPressure(0,0,0)));

        BloodPressure minBp  = judgeBPStatus(weekBpList.stream().min(Comparator.comparingInt(BloodPressure::getSystolic))
                .orElseGet(() ->new BloodPressure(0,0,0)));

        BloodPressure averageBp = judgeBPStatus(StatisticsUtil.calculateAverage(weekBpList));

        List<BpSummary> summaries = calculateAverage(bpHistoryList);

        return new WeekBpRes(graphBpList, maxBp, minBp, averageBp, StatisticsUtil.getPulsePressure(summaries), StatisticsUtil.getArterialPressure(summaries));
    }

    // TODO 해당 로직 리팩토링 요망
    @Transactional(readOnly = true)
    public ContinuityWeekRes continuity(Long patientSeq) {

        int continuityCount = 0;
        boolean isFailed = false;

        LocalDateTime fromDateTime = LocalDateTime.of(LocalDate.now().minusDays(55), LocalTime.MIN);
        LocalDateTime toDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59, 9999));

        List<BpHistory> bpList = getByPatientSeqAndIsUsableIsTrueAndMeasureTimeBetween(patientSeq, fromDateTime, toDateTime);
        List<BpSummary> summaries = calculateAverage(bpList);

        for (int i = 0; i < summaries.size(); i++) {

        }


        if (bpList.size() == 0) {
            return new ContinuityWeekRes(0, false);
        }

        List<BpSummary> bpSummaryList = calculateAverage(bpList);

        // 56일짜리 빈날짜가 없는 Map 초기화
        TreeMap<LocalDate, BpSummary> days56Map = new TreeMap<>();

        for (int i = 0; i < 56; i++) {
            days56Map.put(fromDateTime.toLocalDate().plusDays(i), null);
        }

        for (BpSummary bpSummary : bpSummaryList) {
            days56Map.put(bpSummary.getSummaryDate(), bpSummary);
        }

        List<BpSummary> bpSummary56List = days56Map.values().stream().toList();

        // 56개의 정렬된 bpSummary56List를 7개씩 잘라 8개(주)의 map으로 변환하고 주별 성공 체크
        Map<Integer, List<BpSummary>> weekBpSummaryMap = new TreeMap<>();

        for (int i = 0; i < bpSummary56List.size(); i += 7) {
            weekBpSummaryMap.put(i/7 + 1, bpSummary56List.subList(i, i + 7));
        }

        Map<Integer, Boolean> weekSuccessMap = new TreeMap<>();

        for (Integer week : weekBpSummaryMap.keySet()) {

            List<BpSummary> bsList = weekBpSummaryMap.get(week);

            int measureDateCountInWeek= (int) bsList.stream()
                    .filter(Objects::nonNull)
                    .filter(bs -> bs.getMeasureCount() > 0).count();

            if(measureDateCountInWeek >= 4) {
                weekSuccessMap.put(week, true);
            }

        }

        // 주간 연속 성공 체크
        for (boolean isSuccess : weekSuccessMap.values()) {
            if (isSuccess) {
                continuityCount++;
            } else {
                if (continuityCount > 0) { // 이전에 성공한 경우가 있지만 다음이 실패한 경우
                    isFailed = true;
                }
                continuityCount = 0; // 연속 성공에 실패 했으므로 0으로 초기화
            }
        }

        return new ContinuityWeekRes(continuityCount, isFailed);
    }

    @Transactional(readOnly = true)
    public List<BpHistory> getByPatientSeqAndIsUsableIsTrueAndMeasureTimeBetween(Long patientSeq, LocalDateTime fromDateTime, LocalDateTime toDateTime) {

        List<BpHistory> list = bpHistoryRepository.findByPatientSeqAndIsUsableIsTrueAndMeasureTimeBetween(patientSeq, fromDateTime, toDateTime);

        if(list.isEmpty()) {
            list = new ArrayList<>();
        }

        return list;
    }

    @Transactional(readOnly = true)
    public List<BpSummary> calculateAverage(List<BpHistory> histories) {

        //convert List<BpHistory> to ConcurrentMap<LocalDate, List<BpHistory>>
        //key : measuredDate || value : histories per key
        Map<LocalDate, List<BpHistory>> groupingByDate = histories.stream()
                .filter(BpHistory::isPredictUsable)
                .collect(Collectors.groupingBy(BpHistory::getMeasureDate, toList()));

        //convert ConcurrentMap<LocalDate, List<BpHistory>> to List<BpSummary>
        List<BpSummary> summaries = groupingByDate.entrySet().stream()
                .map(d -> {
                    LocalDate date = d.getKey();
                    List<BpHistory> value = d.getValue();

                    int week = value.get(0).getWeek();
                    long dangerCount = value.stream().filter(BpHistory::isDanger).count();

                    List<BloodPressure> bps = value.stream()
                            .flatMap(bpHistory -> Stream.of(bpHistory.getFirstBP(), bpHistory.getSecondBP())).toList();

                    double systolic = bps.stream().collect(Collectors.summarizingDouble(BloodPressure::getSystolic)).getAverage();
                    double diastolic = bps.stream().collect(Collectors.summarizingDouble(BloodPressure::getDiastolic)).getAverage();
                    double pulse = bps.stream().collect(Collectors.summarizingDouble(BloodPressure::getPulse)).getAverage();

                    return new BpSummary(week, date, bps.size(), (int) dangerCount,
                            (int) Math.round(systolic), (int) Math.round(diastolic), (int) Math.round(pulse), value);
                }).toList();

        return summaries;
    }

    @Transactional
    public void modify(BpHistoryModifyReq request) {

        BpHistory bpHistory = bpHistoryRepository.findById(request.getSeq()).orElseThrow(
                () -> new BusinessException(ResponseStatus.NOT_EXIST_BP, HttpStatus.NOT_FOUND)
        );

        Patient patient = bpHistory.getPatient();
        LocalDate firstMeasuredDate = patient.getFirstMeasuredDate();
        LocalDate lastMeasuredDate = patient.getLastMeasuredDate();


        LocalDateTime measuredAt = request.getMeasureTime();
        LocalDate measuredDate = measuredAt.toLocalDate();
        int measureHour = measuredAt.getHour();

        if (measureHour >= 4 && measureHour <= 12) {
            request.setUsable(true);
        }

        bpHistory.modify(judgeBPStatus(request.getFirstBP()), judgeBPStatus(request.getSecondBP()),
                measuredAt, request.isUsable());

        syncPatientMeasuredInfo(patient);

    }

    @Transactional
    public void delete(Long seq) {

        BpHistory bpHistory = bpHistoryRepository.findById(seq).orElseThrow(() -> new BusinessException(ResponseStatus.NOT_EXIST_BP, HttpStatus.NOT_FOUND));

        Patient patient = bpHistory.getPatient();

        bpHistoryRepository.delete(bpHistory);

        syncPatientMeasuredInfo(patient);

        updateMeasureStatus(patient);

    }

    public void syncPatientMeasuredInfo(Patient patient) {
        List<BpHistory> histories = bpHistoryRepository.findByPatientAndIsUsableIsTrueOrderByMeasureTimeDesc(patient);

        LocalDate newFirstMeasuredDate, newLastMeasuredDate;

        if(histories.isEmpty()) {
            newFirstMeasuredDate = null;
            newLastMeasuredDate = null;
        }
        else {
            newFirstMeasuredDate = histories.get(histories.size() - 1).getMeasureDate();
            newLastMeasuredDate = histories.get(0).getMeasureDate();
        }

        patient.changeFirstMeasuredDate(newFirstMeasuredDate);
        patient.changeLastMeasuredDate(newLastMeasuredDate);
    }

    @Transactional(readOnly = true)
    public BpStatisticsPerUnitRes statistics(Long patientSeq, Unit unit) {

        Patient patient = patientService.findById(patientSeq);

        if(Objects.isNull(patient.getFirstMeasuredDate())) {
            throw new BusinessException(ResponseStatus.NOT_EXIST_BP, HttpStatus.NOT_FOUND);
        }

        LocalDateTime standardFrom = getStandardFrom(patient);

        List<BpHistory> histories = bpHistoryRepository.findByPatientSeqAndIsUsableIsTrueAndMeasureTimeBetween(patientSeq, standardFrom, NOW_LOCAL_DATE_TIME_FOR_NOW);
        List<BpSummary> summaries = calculateAverage(histories);

        Map<PeriodVO, Treatment> treatmentMap = treatmentService.getTreatmentSeparateFromDate(patientSeq);

        Map<Integer, List<BpSummary>> separatePerUnit = summaries.stream()
                .collect(Collectors.groupingBy(i -> {
                            int week = judgeWeek(standardFrom.toLocalDate(), i.getSummaryDate());
                            return switch (unit) {
                                case WEEK -> week;
                                case MONTH -> ((week -1) / 4) + 1;
                            };
                        },
                        toList()));

        List<BpStatisticsRes> resultList = separatePerUnit.entrySet().stream().map(i -> {
            int standard = i.getKey();
            List<BpSummary> summariesPerUnit = i.getValue();
            BpSummary lastSummary = summariesPerUnit.get(summariesPerUnit.size() - 1);
            Treatment treatment = treatmentMap.entrySet()
                    .stream()
                    .filter(t -> t.getKey().isContains(lastSummary.getSummaryDate()))
                    .findFirst()
                    .orElseThrow(() -> new BusinessException(ResponseStatus.NOT_EXIST_TREATMENT, HttpStatus.BAD_REQUEST))
                    .getValue();

            BPSummaryVO averages = StatisticsUtil.getAverages(summariesPerUnit);
            BPSummaryVO standardDeviation = StatisticsUtil.getStandardDeviation(summariesPerUnit);
            BPSummaryVO relativeStandardDeviation = StatisticsUtil.getRelativeStandardDeviation(summariesPerUnit);
            BPReachRateVO reachRatePerState = StatisticsUtil.getReachRatePerState(treatment, summariesPerUnit);
            int alarmCount = summariesPerUnit.stream().mapToInt(BpSummary::getAlarmCount).sum();

            return new BpStatisticsRes(standard, averages, standardDeviation, relativeStandardDeviation, reachRatePerState, alarmCount);
        }).toList();

        BpStatisticsPerUnitRes result = new BpStatisticsPerUnitRes(unit, resultList);

        return result;

    }



    public LocalDateTime getStandardFrom(Patient patient) {
        LocalDateTime from;
        LocalDate firstMeasuredDate = patient.getFirstMeasuredDate();
        int measuredDateCount = (int) Duration.between(firstMeasuredDate.atStartOfDay(), LocalDate.now().atStartOfDay()).toDays();

        //첫 측정일로부터 56일이 지났으면 최근 56일치 데이터, 지나지 않았다면 첫 측정일 이 후 모든 데이터
        from = measuredDateCount >= 55 ? LocalDateTime.of(LocalDate.now().minusDays(55), LocalTime.MIN) : LocalDateTime.of(firstMeasuredDate, LocalTime.MIN);

        return from;
    }

    public List<BpHistory> getHistory7Times(long patientSeq) {
        List<BpHistory> timesHistory = bpHistoryRepository.findTop4ByPatientOrderByMeasureTimeAsc(patientService.findById(patientSeq));
        return timesHistory;
    }

    public BpHistoryReportRes report(Long patientSeq) {

        /**
         * 7 times check
         */
        List<BpHistory> history7Times = getHistory7Times(patientSeq);
        history7Times.forEach(System.out::println);
        List<BpSummary> timesSummary = history7Times.stream().flatMap(
                i -> Stream.of(
                        new BpSummary(i.getWeek(), i.getMeasureDate(), 0, 0, new BloodPressure(i.getFirstBP().getSystolic(), i.getFirstBP().getDiastolic(), i.getFirstBP().getPulse()), null),
                        new BpSummary(i.getWeek(), i.getMeasureDate(), 0, 0, new BloodPressure(i.getSecondBP().getSystolic(), i.getSecondBP().getDiastolic(), i.getSecondBP().getPulse()), null)
                )).toList();


        timesSummary = timesSummary.subList(1, 8); //7개 맞추기 위해 맨 앞 제거

        BPSummaryVO timesAverages = getAverages(timesSummary);
        int timesPulse = (int) timesSummary.stream().mapToInt(i -> i.getBloodPressure().getPulse()).average().orElse(0);
        int timePulsePressure = (int) getPulsePressure(timesSummary);
        int timeArterialPressure = (int) getArterialPressure(timesSummary);

        /**
         * 1 week check
         */
        List<BpHistory> weekHistory = getByPatientSeqAndIsUsableIsTrueAndMeasureTimeBetween(patientSeq, LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusDays(6), LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
        List<BpSummary> weekSummary = calculateAverage(weekHistory);

        BPSummaryVO weekAverages = getAverages(weekSummary);
        int weekPulse = (int) timesSummary.stream().mapToInt(i -> i.getBloodPressure().getPulse()).average().orElse(0);
        int weekPulsePressure = (int) getPulsePressure(weekSummary);
        int weekArterialPressure = (int) getArterialPressure(weekSummary);

        /**
         * 1 month check
         */
        List<BpHistory> monthHistory = getByPatientSeqAndIsUsableIsTrueAndMeasureTimeBetween(patientSeq, LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusDays(29), LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
        List<BpSummary> monthSummary = calculateAverage(monthHistory);

        BPSummaryVO monthAverages = getAverages(monthSummary);
        int monthPulse = (int) timesSummary.stream().mapToInt(i -> i.getBloodPressure().getPulse()).average().orElse(0);
        int monthPulsePressure = (int) getPulsePressure(monthSummary);
        int monthArterialPressure = (int) getArterialPressure(monthSummary);


        return new BpHistoryReportRes(
                new BpHistoryAveragesRes((int)timesAverages.getSystolicSummary(), (int)timesAverages.getDiastolicSummary(), timesPulse, timePulsePressure, timeArterialPressure),
                new BpHistoryAveragesRes((int)weekAverages.getSystolicSummary(), (int)weekAverages.getDiastolicSummary(), weekPulse, weekPulsePressure, weekArterialPressure),
                new BpHistoryAveragesRes((int)monthAverages.getSystolicSummary(), (int)monthAverages.getDiastolicSummary(), monthPulse, monthPulsePressure, monthArterialPressure));
    }
}