package kr.lifesemantics.canofymd.moduleapi.domain.staff.mapper;

import kr.lifesemantics.canofymd.moduleapi.domain.staff.dto.res.StaffDetailRes;
import kr.lifesemantics.canofymd.moduleapi.domain.staff.dto.res.StaffListRes;
import kr.lifesemantics.canofymd.modulecore.domain.user.Staff;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StaffMapper {

     public StaffListRes toStaffListRes(Staff staff) {
         return StaffListRes.builder()
                                .seq(staff.getSeq())
                                .id(staff.getId())
                                .name(staff.getName())
                                .userType(staff.getUserType())
                                .contact(staff.getContact())
                                .createdAt(staff.getCreatedAt())
                                .isActive(staff.isActive())
                            .build();
     }

    public StaffDetailRes toStaffDetailRes(Staff staff) {
        return StaffDetailRes.builder()
                                    .seq(staff.getSeq())
                                    .id(staff.getId())
                                    .name(staff.getName())
                                    .userType(staff.getUserType())
                                    .contact(staff.getContact())
                                    .createdAt(staff.getCreatedAt())
                                    .isActive(staff.isActive())
                             .build();
    }

}
