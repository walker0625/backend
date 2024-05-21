package kr.lifesemantics.canofymd.moduleapi.domain.patient.mapper;

import kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.res.PatientBasicInfo;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.res.PatientDetailRes;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.res.PatientFindListRes;
import kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.res.PatientPersonalRes;
import kr.lifesemantics.canofymd.modulecore.domain.user.Patient;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.patient.mapper
 * fileName       : PatientMapper
 * author         : ms.jo
 * date           : 2024/04/25
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/04/25        ms.jo       최초 생성
 */

@Configuration
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PatientMapper {


    public PatientFindListRes toPatientFindListRes(Patient patient) {
        return PatientFindListRes
                .builder()
                .seq(patient.getSeq())
                .id(patient.getId())
                .name(patient.getName())
                .birthDate(patient.getBirthDate())
                .contact(patient.getContact())
                .gender(patient.getGender())
                .createdAt(patient.getCreatedAt())
                .status(patient.getStatus())
                .build();
    }

    public PatientDetailRes toPatientDetailRes(Patient patient) {
        return PatientDetailRes.builder()
                .seq(patient.getSeq())
                .id(patient.getId())
                .name(patient.getName())
                .birthDate(patient.getBirthDate())
                .contact(patient.getContact())
                .gender(patient.getGender())
                .createdAt(patient.getCreatedAt())
                .height(patient.getHeight())
                .weight(patient.getWeight())
                .isDrink(patient.isDrink())
                .isSmoke(patient.isSmoke())
                .isFamilyDisease(patient.isFamilyDisease())
                .build();
    }

    public PatientBasicInfo toPatientBasicInfo(Patient patient) {
        return PatientBasicInfo.builder()
                .seq(patient.getSeq())
                .gender(patient.getGender())
                .birthDate(patient.getBirthDate())
                .age(patient.getAge())
                .bmi(patient.getBMI())
                .isSmoke(patient.isSmoke())
                .contact(patient.getContact())
                .build();
    }

    public PatientPersonalRes toPatientPersonalRes(Patient patient) {
        return new PatientPersonalRes(patient.getId(), patient.getName(), patient.getContact(), patient.getGender(),
                patient.getBirthDate(), patient.getCreatedAt().toLocalDate(), patient.getHeight(), patient.getWeight(), patient.isDrink(), patient.isSmoke());
//        return PatientPersonalRes.builder()
//                .id(patient.getId())
//                .name(patient.getName())
//                .contact(patient.getContact())
//                .gender(patient.getGender())
//                .birthDate(patient.getBirthDate())
//                .createdDate(patient.getCreatedAt().toLocalDate())
//                .height(patient.getHeight())
//                .weight(patient.getWeight())
//                .isDrink(patient.isDrink())
//                .isSmoke(patient.isSmoke())
//                .build();
    }
}
