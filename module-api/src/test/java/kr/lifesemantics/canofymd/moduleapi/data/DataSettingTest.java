package kr.lifesemantics.canofymd.moduleapi.data;

import kr.lifesemantics.canofymd.moduleapi.domain.hospital.dto.req.HospitalRegisterReq;
import kr.lifesemantics.canofymd.moduleapi.domain.hospital.dto.res.HospitalFindListRes;
import kr.lifesemantics.canofymd.moduleapi.domain.hospital.service.HospitalService;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.req.PatientRegisterReq;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.res.PatientFindListRes;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.service.PatientService;
import kr.lifesemantics.canofymd.moduleapi.domain.staff.dto.req.StaffRegisterReq;
import kr.lifesemantics.canofymd.moduleapi.domain.staff.dto.res.StaffListRes;
import kr.lifesemantics.canofymd.moduleapi.domain.staff.service.StaffService;
import kr.lifesemantics.canofymd.modulecore.constants.DefaultValues;
import kr.lifesemantics.canofymd.modulecore.domain.user.Hospital;
import kr.lifesemantics.canofymd.modulecore.domain.user.Patient;
import kr.lifesemantics.canofymd.modulecore.domain.user.Staff;
import kr.lifesemantics.canofymd.modulecore.enums.Category;
import kr.lifesemantics.canofymd.modulecore.enums.Gender;
import kr.lifesemantics.canofymd.modulecore.enums.UserType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

@SpringBootTest
@ActiveProfiles("local")
class DataSettingTest {

    private static final Logger log = LoggerFactory.getLogger(DataSettingTest.class);
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired StaffService staffService;
    @Autowired HospitalService hospitalService;
    @Autowired PatientService patientService;

    private final String rawPassword = DefaultValues.DEFAULT_PASSWORD;
    private Predicate<Integer> isOdd() {
        return (i) -> i % 2 == 1;
    }

    @Rollback(value = false)
    @Test
    void 사용자_데이터_생성() {

        StaffListRes register = staffService.register(createAdminReq());
        Staff staff = staffService.findById(register.getSeq());

        HospitalFindListRes hospitalFindListRes = hospitalService.create(createHospitalReq());
        Hospital hospital = hospitalService.findById(hospitalFindListRes.getSeq());

        StaffListRes allDoctorRes = staffService.register(createAllDoctorReq(hospital));
        StaffListRes bpDoctorRes = staffService.register(createBPDoctorReq(hospital));
        StaffListRes scDoctorRes = staffService.register(createSCDoctorReq(hospital));

        Staff allDoctor = staffService.findById(allDoctorRes.getSeq());
        Staff bpDoctor = staffService.findById(bpDoctorRes.getSeq());
        Staff scDoctor = staffService.findById(scDoctorRes.getSeq());

        List<PatientRegisterReq> patientResses = createPatientReqs();
        List<Patient> patients = new ArrayList<>();
        IntStream.range(0, patientResses.size() - 1).forEach(i -> {
            log.info("{}", i);
            PatientFindListRes patientFindListRes = patientService.create(hospital.getSeq(), scDoctor.getSeq(), isOdd().test(i) ? Category.BPAI : Category.SCAI, patientResses.get(i));
            patients.add(patientService.findById(patientFindListRes.getSeq()));
        });

    }

    private List<PatientRegisterReq> createPatientReqs() {
        List<PatientRegisterReq> patients = new ArrayList<>();

        IntStream.range(1, 12).forEach(i -> {
            PatientRegisterReq patientRegisterReq = new PatientRegisterReq("patient" + i, "환자" + i, isOdd().test(i) ? Gender.FEMALE : Gender.MALE, LocalDate.of(1944, 4, 4), "010-4444-4444", 174.4, 74.4, true, true, true);

            patients.add(patientRegisterReq);
        });

        return patients;
    }

    public StaffRegisterReq createAllDoctorReq(Hospital hospital) {
        return new StaffRegisterReq("doctor1", rawPassword, "둘다의사", UserType.DOCTOR, "010-1111-2222", hospital.getSeq(), List.of(Category.values()));
    }

    public StaffRegisterReq createBPDoctorReq(Hospital hospital) {
        return new StaffRegisterReq("doctor2", rawPassword, "BP의사", UserType.DOCTOR, "010-1111-2222", hospital.getSeq(), List.of(Category.BPAI));
    }

    public StaffRegisterReq createSCDoctorReq(Hospital hospital) {
        return new StaffRegisterReq("doctor3", rawPassword, "SC의사", UserType.DOCTOR, "010-1111-2222", hospital.getSeq(), List.of(Category.SCAI));
    }

    public HospitalRegisterReq createHospitalReq() {
        return new HospitalRegisterReq("메디힐 정형외과", "최준호", "010-1163-8943", "서울시 강남구 언주로 533 12층");
    }

    public StaffRegisterReq createAdminReq() {
        return new StaffRegisterReq("admin1", rawPassword, "관리자", UserType.ADMIN, "010-1111-2222", null, null);
    }


    public Patient createBPAIPatient(Hospital hospital) {
        Patient patient = Patient.create("bp-patient", DefaultValues.DEFAULT_PASSWORD, "BP환자", "010-4444-4444", Gender.MALE, LocalDate.of(1944, 4, 4), 174.4, 74.4, true, true, true, Category.BPAI, hospital, null);
        return patient;
    }

    public Patient createBPAIPatient(Hospital hospital, Staff staff) {
        Patient patient = Patient.create("sc-patient", DefaultValues.DEFAULT_PASSWORD, "SC환자", "010-4444-4444", Gender.MALE, LocalDate.of(1944, 4, 4), 174.4, 74.4, true, true, true, Category.SCAI, hospital, staff);
        return patient;
    }

}
