package kr.lifesemantics.canofymd.moduleapi.domain.staff.dto.res;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.lifesemantics.canofymd.modulecore.enums.UserType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(title = "병원관계자 정보 조회 응답 DTO")
public class StaffListRes {

    @Schema(description = "병원관계자 순차번호", example = "1")
    Long seq;

    @Schema(description = "아이디", example = "doctor1")
    String id;

    @Schema(description = "이름", example = "김의사")
    String name;

    @Schema(description = "사용자 구분", example = "DOCTOR")
    UserType userType;

    @Schema(description = "연락처", example = "010-1111-1111")
    String contact;

    @Schema(description = "등록일", example = "2024-03-19T13:24:02.1008277")
    LocalDateTime createdAt;

    @Schema(description = "활성화 여부", example = "true")
    boolean isActive;

    @Builder
    @QueryProjection
    public StaffListRes(Long seq, String id, String name, UserType userType, String contact, LocalDateTime createdAt, boolean isActive) {
        this.seq = seq;
        this.id = id;
        this.name = name;
        this.userType = userType;
        this.contact = contact;
        this.createdAt = createdAt;
        this.isActive = isActive;
    }

}
