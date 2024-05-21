package kr.lifesemantics.canofymd.moduleapi.domain.bp.prediction.repository;

import kr.lifesemantics.canofymd.modulecore.domain.bp.predication.Predication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.bp.prediction.repository
 * fileName       : PredicationRepository
 * author         : ms.jo
 * date           : 2024/05/03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/05/03        ms.jo       최초 생성
 */
@Repository
public interface PredicationRepository extends JpaRepository<Predication, Long> {

    List<Predication> findByPatientSeqIn(List<Long> patientSeqList);

}
