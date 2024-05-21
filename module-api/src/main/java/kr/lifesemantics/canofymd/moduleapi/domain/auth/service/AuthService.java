package kr.lifesemantics.canofymd.moduleapi.domain.auth.service;

import kr.lifesemantics.canofymd.moduleapi.domain.auth.dto.res.ChangePwYNRes;
import kr.lifesemantics.canofymd.moduleapi.domain.auth.dto.res.JwtIssueRes;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.repository.PatientRepository;
import kr.lifesemantics.canofymd.moduleapi.domain.staff.repository.StaffRepository;
import kr.lifesemantics.canofymd.modulecore.domain.user.Patient;
import kr.lifesemantics.canofymd.modulecore.domain.user.Staff;
import kr.lifesemantics.canofymd.modulecore.domain.user.StaffCategory;
import kr.lifesemantics.canofymd.modulecore.enums.AuthType;
import kr.lifesemantics.canofymd.modulecore.enums.UserType;
import kr.lifesemantics.canofymd.modulecore.exception.BusinessException;
import kr.lifesemantics.canofymd.modulecore.util.JwtUtil;
import kr.lifesemantics.canofymd.modulecore.util.ResponseStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class AuthService {

    StaffRepository staffRepository;
    PatientRepository patientRepository;

    PasswordEncoder passwordEncoder;
    JwtUtil jwtUtil;

    public JwtIssueRes issueStaffJwt(String id, String requestPassword) {

        Staff staff = staffRepository.findByStaffId(id);

        if (staff == null) {
            throw new BusinessException(ResponseStatus.NOT_EXIST_STAFF, HttpStatus.UNAUTHORIZED);
        }

//        if(staff.getUserType() != UserType.ADMIN && !staff.getCategories().stream().filter(f -> f.getCategory().equals(category)).findAny().isPresent()) {
//            throw new BusinessException(ResponseStatus.NOT_EXIST_STAFF, HttpStatus.UNAUTHORIZED);
//        }

        if (!passwordEncoder.matches(requestPassword, staff.getPassword())) {
            throw new BusinessException(ResponseStatus.FAILED_MATCH_PASSWORD, HttpStatus.UNAUTHORIZED);
        }

        // TODO 1440분 = 1일로 설정(리프레시 토큰 추가시 30분으로 변경)
        return new JwtIssueRes(jwtUtil.createJwt(AuthType.STAFF, staff.getUserType(), staff.getId(), staff.getName(), staff.getSeq(), staff.getUserType() == UserType.ADMIN ? null : staff.getHospital().getSeq(), 1440L, staff.getCategories().stream().map(StaffCategory::getCategory).toList()));
    }

    public JwtIssueRes issuePatientJwt(String id, String requestPassword, String pushToken) {

        Patient patient = patientRepository.findByPatientId(id);

        if (patient == null) {
            throw new BusinessException(ResponseStatus.NOT_EXIST_PATIENT, HttpStatus.UNAUTHORIZED);
        }

        patient.updatePushToken(pushToken);

        if (!passwordEncoder.matches(requestPassword, patient.getPassword())) {
            throw new BusinessException(ResponseStatus.FAILED_MATCH_PASSWORD, HttpStatus.UNAUTHORIZED);
        }

        // TODO 1440분 = 1일로 설정(리프레시 토큰 추가시 30분으로 변경)
        return new JwtIssueRes(jwtUtil.createJwt(AuthType.PATIENT, UserType.PATIENT, patient.getId(), patient.getName(), patient.getSeq(), patient.getHospital().getSeq(), 1440L, List.of(patient.getCategory().getCategory())));
    }

    public ChangePwYNRes checkAlreadyChangePassword(AuthType authType, Long seq) {

        // false로 초기화하면 로그인 자체가 안될 수 있어 true로 초기화
        boolean isPasswordChange = true;

        if(authType.equals(AuthType.STAFF)) {
            isPasswordChange =  staffRepository.findById(seq).get().isPasswordChange();
        } else if(authType.equals(AuthType.PATIENT)) {
            isPasswordChange  = patientRepository.findById(seq).get().isPasswordChange();
        }

        return new ChangePwYNRes(isPasswordChange);
    }

}