package kr.lifesemantics.canofymd.moduleapi.domain.bp.history.repository;

import kr.lifesemantics.canofymd.modulecore.domain.bp.BpHistory;
import kr.lifesemantics.canofymd.modulecore.domain.user.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BpHistoryRepository extends JpaRepository<BpHistory, Long>, BpHistoryCustomRepository {

    BpHistory findFirstByPatientSeqOrderByMeasureTimeDesc(Long patientSeq);

    List<BpHistory> findByPatientSeqOrderByMeasureTimeDesc(Long patientSeq);

    List<BpHistory> findByPatientSeqAndIsUsableIsTrueAndMeasureTimeBetween(Long patientSeq, LocalDateTime from, LocalDateTime to);

    List<BpHistory> findByPatientSeqAndMeasureTimeBetween(Long patientSeq, LocalDateTime fromDateTime, LocalDateTime toDateTime);

    List<BpHistory> findByPatientSeqInAndIsUsableIsTrueAndMeasureDateBetween(List<Long> patientSeq, LocalDate fromDate, LocalDate toDate);

    List<BpHistory> findByPatientSeqAndWeek(Long patientSeq, int lastWeek);

    List<BpHistory> findByPatientSeqInAndMeasureDateBetween(List<Long> patientSeqList, LocalDate twodaysago, LocalDate today);

    List<BpHistory> findByPatientAndIsUsableIsTrueOrderByMeasureTimeDesc(Patient patient);

    List<BpHistory> findTop4ByPatientOrderByMeasureTimeAsc(Patient byId);
}
