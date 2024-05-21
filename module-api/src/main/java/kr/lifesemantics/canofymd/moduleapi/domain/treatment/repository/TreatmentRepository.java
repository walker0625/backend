package kr.lifesemantics.canofymd.moduleapi.domain.treatment.repository;

import kr.lifesemantics.canofymd.modulecore.domain.bp.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.treatment.repository
 * fileName       : TreatmentRepository
 * author         : ms.jo
 * date           : 2024/04/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/04/26        ms.jo       최초 생성
 */
@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, Long> {

    List<Treatment> findByPatientSeqOrderByTreatedAtDesc(Long patientSeq);

    List<Treatment> findByPatientSeqOrderByTreatedAtAsc(Long patientSeq);

    Optional<Treatment> findFirstByPatientSeqOrderByTreatedAtDesc(Long patientSeq);
}
