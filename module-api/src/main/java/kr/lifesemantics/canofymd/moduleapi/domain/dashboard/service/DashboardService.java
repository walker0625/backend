package kr.lifesemantics.canofymd.moduleapi.domain.dashboard.service;

import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.repository.BpHistoryRepository;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.prediction.repository.PredicationRepository;
import kr.lifesemantics.canofymd.moduleapi.domain.dashboard.dto.res.DashboardPeriodRes;
import kr.lifesemantics.canofymd.moduleapi.domain.dashboard.dto.res.DashboardStatisticsRes;
import kr.lifesemantics.canofymd.moduleapi.domain.hospital.enums.TransitionDirection;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.repository.PatientRepository;
import kr.lifesemantics.canofymd.moduleapi.global.util.StatisticsUtil;
import kr.lifesemantics.canofymd.modulecore.domain.bp.BpHistory;
import kr.lifesemantics.canofymd.modulecore.domain.bp.predication.Predication;
import kr.lifesemantics.canofymd.modulecore.domain.user.Patient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequiredArgsConstructor
public class DashboardService {

    PatientRepository patientRepository;
    BpHistoryRepository bpHistoryRepository;
    PredicationRepository predicationRepository;

    @Transactional(readOnly = true)
    public DashboardStatisticsRes statistics(Long hospitalSeq) {

        LocalDate today = LocalDate.now();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate twoDaysAgo = LocalDate.now().minusDays(2);

        List<Patient> patientList = patientRepository.findByHospitalSeq(hospitalSeq);
        List<Long> patientSeqList = patientList.stream().map(Patient::getSeq).toList();

        List<BpHistory> bpList = bpHistoryRepository.findByPatientSeqInAndMeasureDateBetween(patientSeqList, twoDaysAgo, today);

        // 가입 이용자 + 증감
        int totalPatient = patientList.size();
        int yesterdayPatient = patientList.stream()
                .filter(patient -> !patient.getCreatedAt().toLocalDate().equals(today))
                .toList().size();

        TransitionDirection totalPatientDirection  = StatisticsUtil.judgeDirection(totalPatient, yesterdayPatient);

        // 어제 측정자 + 증감
        int yesterdayMeasurePatient = bpList.stream()
                .filter(patient -> patient.getMeasureDate().equals(yesterday))
                .map(bpHistory -> bpHistory.getPatient().getSeq())
                .collect(Collectors.toSet()).size();

        int twodaysagoMeasurePatient = bpList.stream()
                .filter(patient -> patient.getMeasureDate().equals(twoDaysAgo))
                .map(bpHistory -> bpHistory.getPatient().getSeq())
                .collect(Collectors.toSet()).size();

        TransitionDirection yesterdayMeasureDirection = StatisticsUtil.judgeDirection(yesterdayMeasurePatient, twodaysagoMeasurePatient);

        // 오늘 측정자 + 증감
        int todayMeasurePatient = bpList.stream()
                .filter(patient -> patient.getMeasureDate().equals(today))
                .map(bpHistory -> bpHistory.getPatient().getSeq())
                .collect(Collectors.toSet()).size();

        TransitionDirection todayMeasureDirection = StatisticsUtil.judgeDirection(todayMeasurePatient, yesterdayMeasurePatient);

        // 누적 혈압 예측자 + 증감
        List<Predication> predicationList = predicationRepository.findByPatientSeqIn(patientSeqList);

        int accumulatePredicationPatient = predicationList.stream()
                .map(predication -> predication.getPatient().getSeq())
                .collect(Collectors.toSet()).size();

        int yesterdayAccumulatePredicationPatient = predicationList.stream()
                .filter(predication -> !predication.getCreatedAt().toLocalDate().equals(today))
                .map(predication -> predication.getPatient().getSeq())
                .collect(Collectors.toSet()).size();


        TransitionDirection accumulatePredicationDirection = StatisticsUtil.judgeDirection(accumulatePredicationPatient, yesterdayAccumulatePredicationPatient);

        return new DashboardStatisticsRes(totalPatient, totalPatientDirection, yesterdayMeasurePatient, yesterdayMeasureDirection,
                todayMeasurePatient, todayMeasureDirection, accumulatePredicationPatient, accumulatePredicationDirection);
    }

    @Transactional(readOnly = true)
    public List<DashboardPeriodRes> period(Long hospitalSeq, LocalDate from, LocalDate to) {

        List<Patient> patientList = patientRepository.findByHospitalSeq(hospitalSeq);
        List<Long> patientSeqList = patientList.stream().map(Patient::getSeq).toList();

        List<BpHistory> bpHistoryList = bpHistoryRepository.findByPatientSeqInAndIsUsableIsTrueAndMeasureDateBetween(patientSeqList, from, to);

        Map<String, Integer> datePatientCountMap = bpHistoryList.stream()
                .collect(Collectors.groupingBy(
                                bp -> bp.getMeasureDate().toString(),
                                Collectors.collectingAndThen(
                                        toList(),
                                        bpHistories -> bpHistories.stream()
                                                .map(bp -> bp.getPatient().getSeq())
                                                .collect(Collectors.toSet()).size()
                                )
                        )
                );

        return datePatientCountMap.entrySet().stream()
                                             .map(entry -> new DashboardPeriodRes(entry.getKey(), entry.getValue()))
                                             .collect(toList());
    }

}