package kr.lifesemantics.canofymd.moduleapi.domain.hospital.mapper;

import kr.lifesemantics.canofymd.moduleapi.domain.hospital.dto.res.HospitalDetailRes;
import kr.lifesemantics.canofymd.moduleapi.domain.hospital.dto.res.HospitalFilterRes;
import kr.lifesemantics.canofymd.moduleapi.domain.hospital.dto.res.HospitalFindListRes;
import kr.lifesemantics.canofymd.modulecore.domain.user.Hospital;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HospitalMapper {

    public HospitalFilterRes toHospitalFilterRes(Hospital hospital) {
        return new HospitalFilterRes(hospital.getSeq(), hospital.getName());
    }

    public HospitalFindListRes toHospitalFindListRes(Hospital hospital) {
        return HospitalFindListRes.builder()
                    .seq(hospital.getSeq())
                    .name(hospital.getName())
                    .managerName(hospital.getManagerName())
                    .managerContact(hospital.getManagerContact())
                    .address(hospital.getAddress())
                    .createdAt(hospital.getCreatedAt())
                    .isActive(hospital.getIsActive())
                .build();
    }

    public HospitalDetailRes toHospitalDetailRes(Hospital hospital) {
        return HospitalDetailRes.builder()
                    .seq(hospital.getSeq())
                    .name(hospital.getName())
                    .managerName(hospital.getManagerName())
                    .managerContact(hospital.getManagerContact())
                    .address(hospital.getAddress())
                    .isActive(hospital.getIsActive())
                    .registerDate(hospital.getCreatedAt().toLocalDate())
                .build();
    }
}
