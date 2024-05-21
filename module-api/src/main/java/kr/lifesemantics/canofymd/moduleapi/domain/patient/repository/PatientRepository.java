package kr.lifesemantics.canofymd.moduleapi.domain.patient.repository;

import kr.lifesemantics.canofymd.modulecore.domain.user.Patient;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>, PatientCustomRepository {

    @Query("SELECT p FROM Patient p WHERE p.id = :id")
    @EntityGraph(attributePaths = "category")
    Patient findByPatientId(@Param("id") String id);

    List<Patient> findByHospitalSeq(Long hospitalSeq);
}
