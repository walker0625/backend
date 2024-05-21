package kr.lifesemantics.canofymd.moduleapi.domain.staff.repository;

import kr.lifesemantics.canofymd.moduleapi.domain.staff.dto.res.StaffListRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StaffCustomRepository {

    Page<StaffListRes> findStaffsByConditionWithPaging(Long hospitalSeq, String keyword, Pageable pageable);

}
