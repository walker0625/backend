package kr.lifesemantics.canofymd.moduleapi.domain.hospital.service;

import kr.lifesemantics.canofymd.moduleapi.domain.hospital.dto.req.HospitalModifyReq;
import kr.lifesemantics.canofymd.moduleapi.domain.hospital.dto.req.HospitalRegisterReq;
import kr.lifesemantics.canofymd.moduleapi.domain.hospital.dto.res.HospitalFilterRes;
import kr.lifesemantics.canofymd.moduleapi.domain.hospital.dto.res.HospitalFindListRes;
import kr.lifesemantics.canofymd.moduleapi.domain.hospital.mapper.HospitalMapper;
import kr.lifesemantics.canofymd.moduleapi.domain.hospital.repository.HospitalRepository;
import kr.lifesemantics.canofymd.modulecore.domain.user.Hospital;
import kr.lifesemantics.canofymd.modulecore.enums.UserType;
import kr.lifesemantics.canofymd.modulecore.exception.BusinessException;
import kr.lifesemantics.canofymd.modulecore.util.ResponseStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final HospitalMapper hospitalMapper;

    public List<HospitalFilterRes> getHospitalFilter() {
        return List.of(new HospitalFilterRes());
    }

    @Transactional(readOnly = true)
    public Hospital findById(Long seq) {
        Hospital hospital = hospitalRepository.findById(seq)
                .orElseThrow(() -> new BusinessException(ResponseStatus.NOT_EXIST_HOSPITAL, HttpStatus.NOT_FOUND));

        return hospital;
    }

    @Transactional(readOnly = true)
    public Page<HospitalFindListRes> findHospitalsByName(String keyword, Pageable pageable) {
        return hospitalRepository.findHospitalsByName(keyword, pageable);
    }

    @Transactional(readOnly = true)
    public List<HospitalFilterRes> filter(UserType userType, Long hospitalSeq) {
        return UserType.ADMIN == userType ?
                hospitalRepository.findAllByIsActiveTrue().stream().map(hospitalMapper::toHospitalFilterRes).toList() :
                List.of(hospitalMapper.toHospitalFilterRes(hospitalRepository.findOneBySeqAndIsActiveTrue(hospitalSeq)));
    }

    @Transactional
    public HospitalFindListRes create(HospitalRegisterReq request) {

        Hospital hospital = Hospital.create(
            request.getName(),
            request.getManagerName(),
            request.getManagerContact(),
            request.getAddress()
        );

        try {
            return hospitalMapper.toHospitalFindListRes(hospitalRepository.save(hospital));
        } catch (Exception e) {
            throw new BusinessException(ResponseStatus.FAILED_REGIST_HOSPITAL, HttpStatus.BAD_REQUEST);
        }

    }

    @Transactional(readOnly = true)
    public Boolean isDuplicatedName(String id) {
        return hospitalRepository.findByName(id) == null ? Boolean.TRUE : Boolean.FALSE;
    }

    @Transactional
    public void modify(HospitalModifyReq request) {

        Hospital hospital = findById(request.getHospitalSeq());

        hospital.modify(
            request.getName(),
            request.getManagerName(),
            request.getManagerContact(),
            request.getAddress(),
            request.getIsActive()
        );
    }

}
