package kr.lifesemantics.canofymd.moduleapi.domain.staff.service;

import kr.lifesemantics.canofymd.moduleapi.domain.hospital.service.HospitalService;
import kr.lifesemantics.canofymd.moduleapi.domain.staff.dto.req.StaffModifyReq;
import kr.lifesemantics.canofymd.moduleapi.domain.staff.dto.req.StaffPasswordModifyReq;
import kr.lifesemantics.canofymd.moduleapi.domain.staff.dto.req.StaffRegisterReq;
import kr.lifesemantics.canofymd.moduleapi.domain.staff.dto.res.StaffListRes;
import kr.lifesemantics.canofymd.moduleapi.domain.staff.mapper.StaffMapper;
import kr.lifesemantics.canofymd.moduleapi.domain.staff.repository.StaffRepository;
import kr.lifesemantics.canofymd.modulecore.constants.DefaultValues;
import kr.lifesemantics.canofymd.modulecore.domain.user.Hospital;
import kr.lifesemantics.canofymd.modulecore.domain.user.Staff;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StaffService {

    HospitalService hospitalService;

    StaffRepository staffRepository;
    StaffMapper staffMapper;

    PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Staff findById(Long seq) {
        return staffRepository.findById(seq)
                .orElseThrow(() -> new BusinessException(ResponseStatus.NOT_EXIST_STAFF, HttpStatus.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Page<StaffListRes> list(Long hospitalSeq, String keyword, Pageable pageable) {
        return staffRepository.findStaffsByConditionWithPaging(hospitalSeq, keyword, pageable);
    }

    @Transactional
    public StaffListRes register(StaffRegisterReq request) {

        Hospital hospital = request.getUserType() == UserType.ADMIN ? null : hospitalService.findById(request.getHospitalSeq());

        request.setPassword(passwordEncoder.encode(DefaultValues.DEFAULT_PASSWORD));

        Staff staff = Staff.create(request.getId(), request.getPassword(), request.getName(),
                                   request.getContact(), request.getUserType(), hospital, request.getCategory());

        try {
            return staffMapper.toStaffListRes(staffRepository.save(staff));
        } catch (Exception exception) {
            throw new BusinessException(ResponseStatus.FAILED_REGISTER_STAFF, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void modify(StaffModifyReq request) {

        Staff staff = findById(request.getStaffSeq());

        staff.modify(request.getName(), request.getContact(), request.getUserType(), request.getIsActive());
    }

    @Transactional(readOnly = true)
    public Boolean isDuplicatedId(String id) {
        return staffRepository.findByStaffId(id) != null ? Boolean.TRUE : Boolean.FALSE;
    }

    @Transactional
    public void passwordModify(StaffPasswordModifyReq request) {

        Staff staff = findById(request.getStaffSeq());

        staff.passwordModify(passwordEncoder.encode(request.getPassword()));
    }

}
