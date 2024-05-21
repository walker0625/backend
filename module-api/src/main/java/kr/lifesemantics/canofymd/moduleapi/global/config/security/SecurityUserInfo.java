package kr.lifesemantics.canofymd.moduleapi.global.config.security;

import kr.lifesemantics.canofymd.modulecore.domain.user.Patient;
import kr.lifesemantics.canofymd.modulecore.domain.user.Staff;
import kr.lifesemantics.canofymd.modulecore.enums.Category;
import kr.lifesemantics.canofymd.modulecore.enums.UserType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SecurityUserInfo {

    private String id;
    private String password;
    private Long seq;
    private Long hospitalSeq;
    private List<Category> categories;
    private UserType userType;

    @Builder
    public SecurityUserInfo(String id, String password, Long seq, Long hospitalSeq, UserType userType, List<Category> categories) {
        this.id = id;
        this.password = password;
        this.seq = seq;
        this.hospitalSeq = hospitalSeq;
        this.userType = userType;
        this.categories = categories;
    }

    public static SecurityUserInfo madeFromStaff(Staff staff, List<Category> categories) {
        return SecurityUserInfo.builder()
                                    .id(staff.getId())
                                    .password(staff.getPassword())
                                    .seq(staff.getSeq())
                                    .hospitalSeq(staff.getUserType() != UserType.ADMIN ? staff.getHospital().getSeq() : null)
                                    .userType(staff.getUserType())
                                    .categories(categories)
                                .build();
    }

    public static SecurityUserInfo madeFromPatient(Patient patient, List<Category> categories) {
        return SecurityUserInfo.builder()
                                    .id(patient.getId())
                                    .password(patient.getPassword())
                                    .seq(patient.getSeq())
                                    .hospitalSeq(patient.getHospital().getSeq())
                                    .userType(UserType.PATIENT)
                                    .categories(categories)
                                .build();
    }

}