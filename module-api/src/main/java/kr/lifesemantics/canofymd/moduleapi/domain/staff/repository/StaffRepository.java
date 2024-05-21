package kr.lifesemantics.canofymd.moduleapi.domain.staff.repository;

import kr.lifesemantics.canofymd.modulecore.domain.user.Staff;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long>, StaffCustomRepository {

    @Query("SELECT s FROM Staff s WHERE s.id = :id")
    @EntityGraph(attributePaths = "categories")
    Staff findByStaffId(@Param("id") String id);

}
