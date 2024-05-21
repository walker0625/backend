package kr.lifesemantics.canofymd.moduleapi.domain.hospital.repository;

import kr.lifesemantics.canofymd.modulecore.domain.user.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long>, HospitalCustomRepository {

    List<Hospital> findAllByIsActiveTrue();

    Hospital findOneBySeqAndIsActiveTrue(Long hospitalSeq);

    Hospital findByName(String id);

}
