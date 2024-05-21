package kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.repository;

import kr.lifesemantics.canofymd.modulecore.domain.sc.Diagnose;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiagnoseRepository extends JpaRepository<Diagnose, Long> {

    @EntityGraph(attributePaths = "patient")
    List<Diagnose> findAllByPatientSeqOrderByCreatedAtDesc(Long patientSeq);

    @EntityGraph(attributePaths = "patient")
    Page<Diagnose> findAllByPatientSeqInAndPatientNameContaining(List<Long> patientSeq, String name, Pageable pageable);

    Diagnose findTopByOrderByGroupSeqDesc();

    List<Diagnose> findTop3ByPatientSeqOrderByCreatedAtDesc(Long patientSeq);

}
