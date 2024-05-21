package kr.lifesemantics.canofymd.moduleapi.domain.bp.summary.repository;

import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.vo.BpSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
/*
public interface BpSummaryRepository extends JpaRepository<BpSummary, Long> {

    List<BpSummary> findByPatientSeqAndSummaryDateBetweenOrderBySummaryDateAsc(Long patientSeq, LocalDate localDate, LocalDate now);

    Optional<BpSummary> findFirstByPatientSeqOrderBySummaryDateDesc(Long patientSeq);

    BpSummary findBySummaryDate(LocalDate now);
}
*/