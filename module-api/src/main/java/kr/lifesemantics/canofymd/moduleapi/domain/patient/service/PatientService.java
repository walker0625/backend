package kr.lifesemantics.canofymd.moduleapi.domain.patient.service;

import kr.lifesemantics.canofymd.moduleapi.domain.hospital.service.HospitalService;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.controller.PatientFindByDoctorListRes;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.req.PatientModifyPasswordReq;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.req.PatientModifyReq;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.req.PatientRegisterReq;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.req.PatientUpdatePushTokenReq;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.res.*;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.mapper.PatientMapper;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.repository.PatientRepository;
import kr.lifesemantics.canofymd.moduleapi.domain.staff.service.StaffService;
import kr.lifesemantics.canofymd.moduleapi.global.fcm.dto.FcmSendReq;
import kr.lifesemantics.canofymd.moduleapi.global.fcm.service.FcmService;
import kr.lifesemantics.canofymd.moduleapi.global.util.SmsUtil;
import kr.lifesemantics.canofymd.modulecore.constants.DefaultValues;
import kr.lifesemantics.canofymd.modulecore.domain.user.Patient;
import kr.lifesemantics.canofymd.modulecore.domain.user.Hospital;
import kr.lifesemantics.canofymd.modulecore.enums.Category;
import kr.lifesemantics.canofymd.modulecore.enums.ParticipationCompliance;
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

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.patient.service
 * fileName       : PatientService
 * author         : ms.jo
 * date           : 2024/04/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/04/24        ms.jo       최초 생성
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PatientService {

    PatientRepository patientRepository;
    PatientMapper patientMapper;
    PasswordEncoder passwordEncoder;
    HospitalService hospitalService;
    StaffService staffService;
    FcmService fcmService;



    //----------------------------- CREATE

    @Transactional
    public PatientFindListRes create(Long hospitalSeq, Long staffSeq, Category category, PatientRegisterReq request) {

        Hospital hospital = hospitalService.findById(hospitalSeq);

        log.info("hospital :: {}", hospital);

        Patient patient = patientRepository.save(Patient.create(
                request.getId(),
                passwordEncoder.encode(DefaultValues.DEFAULT_PASSWORD),
                request.getName(),
                request.getContact(),
                request.getGender(),
                request.getBirthDate(),
                request.getWeight(),
                request.getHeight(),
                request.isSmoke(),
                request.isDrink(),
                request.isFamilyDisease(),
                category,
                hospital,
                category == Category.SCAI ? staffService.findById(staffSeq) : null
        ));

        Patient savePatient = Optional.of(patientRepository.save(patient)).orElseThrow
                (() -> new BusinessException(ResponseStatus.FAILED_REGISTER_PATIENT, HttpStatus.BAD_REQUEST));

        return patientMapper.toPatientFindListRes(savePatient);

    }

    //----------------------------- READ

    @Transactional(readOnly = true)
    public Patient findById(Long patientSeq) {
        Patient patient = patientRepository.findById(patientSeq).orElseThrow(() -> new BusinessException(ResponseStatus.NOT_EXIST_PATIENT, HttpStatus.NO_CONTENT));
        return patient;
    }

    @Transactional(readOnly = true)
    public List<Patient> findByIds(List<Long> patientSeqList) {
        List<Patient> patients = patientRepository.findAllById(patientSeqList);
        return patients;
    }

    @Transactional(readOnly = true)
    public Page<PatientFindListRes> getPatientsFromHospital(UserType userType, Category category, Long hospitalSeq, List<ParticipationCompliance> filters, String keyword, Pageable pageable) {

        return patientRepository.findPatientsByFilterWithPaging(userType, category, hospitalSeq, filters, keyword, pageable);

    }

    @Transactional(readOnly = true)
    public Page<PatientFindByDoctorListRes> getPatientsFromDoctor(Category category, Long doctorSeq, String keyword, Pageable pageable) {

        return patientRepository.findPatientsByDoctorPaging(category, doctorSeq, keyword, pageable);
    }

    @Transactional(readOnly = true)
    public List<PatientParticipationDangerousListRes> getParticipationDangerousPatients(UserType userType, Long hospitalSeq, Category category) {

        return patientRepository.findByHospitalSeqAndStatus(userType, hospitalSeq, ParticipationCompliance.DANGEROUS, category);
    }

    @Transactional(readOnly = true)
    public boolean duplicateCheckId(String id) {
        return Objects.isNull(patientRepository.findByPatientId(id));
    }

    @Transactional(readOnly = true)
    public PatientDetailRes getPatientDetail(Long patientSeq) {
        Patient patient = findById(patientSeq);
        return patientMapper.toPatientDetailRes(patient);

    }

    @Transactional(readOnly = true)
    public PatientBasicInfo getBasicInfo(Long seq) {
        Patient patient = findById(seq);
        return patientMapper.toPatientBasicInfo(patient);
    }

    @Transactional(readOnly = true)
    public List<PatientBasicInfo> getBasicInfos(List<Long> seqList) {
        List<Patient> patients = findByIds(seqList);
        return patients.stream().map(i -> patientMapper.toPatientBasicInfo(i)).toList();
    }

    @Transactional(readOnly = true)
    public PatientPersonalRes getPatientPersonalDetail(Long patientSeq) {
        Patient patient = findById(patientSeq);
        return patientMapper.toPatientPersonalRes(patient);
    }

    //----------------------------- PATCH

    public void modify(Long patientSeq, PatientModifyReq request) {
        Patient patient = findById(patientSeq);

        try {
            patient.modify(
                    request.getName(),
                    request.getContact(),
                    request.getGender(),
                    request.getBirthDate(),
                    request.getWeight(),
                    request.getHeight(),
                    request.isSmoke(),
                    request.isDrink(),
                    request.isFamilyDisease()
            );
        } catch (Exception e) {
            throw new BusinessException(ResponseStatus.FAILED_MODIFY_PATIENT, HttpStatus.BAD_REQUEST);
        }
    }

    public void resetPassword(Long patientSeq) {
        Patient patient = findById(patientSeq);
        patient.resetPassword(passwordEncoder.encode(DefaultValues.DEFAULT_PASSWORD));
    }

    public void changePassword(Long patientSeq, PatientModifyPasswordReq request) {
        Patient patient = findById(patientSeq);
        try {
            patient.changePassword(passwordEncoder.encode(request.getPassword()));
        } catch (Exception e) {
            throw new BusinessException(ResponseStatus.FAILED_MODIFY_PASSWORD, HttpStatus.BAD_REQUEST);
        }
    }

    public void updatePushToken(Long patientSeq, PatientUpdatePushTokenReq request) {
        Patient patient = findById(patientSeq);
        patient.updatePushToken(request.getToken());
    }

    public void sendSmsForAppStoreLink(Long patientSeq) {
        Patient patient = findById(patientSeq);

        try {
            SmsUtil.sendSmsForAppStoreLink(patient.getContact().replace("-", ""));
        } catch (IOException e) {
            throw new BusinessException(ResponseStatus.FAILED_SEND_SMS, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void sendCheerUp(UserType userType, Long hospitalSeq, Long patientSeq, Category category) {
        List<PatientParticipationDangerousListRes> dangerousPatients = getParticipationDangerousPatients(userType, hospitalSeq, category);

        List<FcmSendReq> pushRequests = patientSeq == null ? dangerousPatients.stream().map(i -> new FcmSendReq(i.getSeq(), i.getName(), i.getPushToken())).toList()
                                                     : List.of(findById(patientSeq)).stream().map(i -> new FcmSendReq(i.getSeq(), i.getName(), i.getPushToken())).toList();

        pushRequests.stream().forEach(i -> CompletableFuture
                .runAsync(() -> fcmService.send(i))
                .handle((body, exception) -> {
                    if (exception != null) {
                        exception.printStackTrace();
                        log.error("Error sending push notification to patient :: {}[{}]", i.getPatientSeq(), i.getName());
                    } else {
                        log.info("Success sending push notification to patient :: {}[{}]", i.getPatientSeq(), i.getName());
                    }
                    return null;
                }));

    }
}
