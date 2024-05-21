package kr.lifesemantics.canofymd.moduleapi.scheduler;

import kr.lifesemantics.canofymd.moduleapi.global.util.StatisticsUtil;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.repository.BpHistoryRepository;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.service.BpHistoryService;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.repository.PatientRepository;
import kr.lifesemantics.canofymd.modulecore.domain.bp.BpHistory;
import kr.lifesemantics.canofymd.modulecore.domain.user.Patient;
import kr.lifesemantics.canofymd.modulecore.enums.ParticipationCompliance;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@EnableScheduling
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ComplianceScheduler {

    BpHistoryService bpHistoryService;

    PatientRepository patientRepository;
    BpHistoryRepository bpHistoryRepository;

    /**
     * 자정(00시 01분)마다 patient 테이블의 status 컬럼 업데이트
     * (중간에 미측정하는 경우 부정확한 status 값이 갱신되지 않음을 방지하기 위함)
     */
    @Transactional
    @Scheduled(cron = "0 1 0 * * ?")
    public void createPatientStatistics() {

        List<Patient> patients = patientRepository.findAll();
        List<BpHistory> bpHistorys = bpHistoryRepository.findAll();

        Map<Patient, List<BpHistory>> patientBpHistoryListMap = patients.stream()
            .collect(Collectors.toMap(
                patient -> patient,
                patient -> bpHistorys.stream()
                                     .filter(bpHistory -> bpHistory.getPatient().getSeq().equals(patient.getSeq()))
                                     .collect(Collectors.toList())
            )
        );

        Map<Patient, ParticipationCompliance> patientBpSummaryListMap = patientBpHistoryListMap.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> {
                    LocalDateTime from = bpHistoryService.getStandardFrom(entry.getKey());
                    return StatisticsUtil.getParticipationCompliance(bpHistoryService.calculateAverage(entry.getValue()),
                        (int) Duration.between(from.toLocalDate().atStartOfDay(), LocalDate.now().atStartOfDay()).toDays());
                }
            )
        );

        // 기존 DB의 status 값과 계산한 status 값이 다르면 update
        patientBpSummaryListMap.forEach((patient, status) -> {
            if (!patient.getStatus().equals(status)) {
               patient.changeStatus(status);
            }
        });
    }

}
