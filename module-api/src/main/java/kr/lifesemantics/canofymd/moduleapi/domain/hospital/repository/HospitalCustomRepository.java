package kr.lifesemantics.canofymd.moduleapi.domain.hospital.repository;

import kr.lifesemantics.canofymd.moduleapi.domain.hospital.dto.res.HospitalFindListRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HospitalCustomRepository {

    Page<HospitalFindListRes> findHospitalsByName(String keyword, Pageable pageable);

}
