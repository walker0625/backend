package kr.lifesemantics.canofymd.moduleapi.domain.staff.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;

import kr.lifesemantics.canofymd.modulecore.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(name = "병원관계자 상세 정보 DTO")
@AllArgsConstructor
public class StaffDetailRes {

    @Schema(description = "병원관계자 순차번호", example = "1")
    Long seq;

    @Schema(description = "아이디", example = "coordinator1")
    String id;

    @Schema(description = "이름", example = "김코디")
    String name;

    @Schema(description = "사용자 구분", example = "COORDINATOR")
    UserType userType;

    @Schema(description = "연락처", example = "010-1111-1111")
    String contact;

    @Schema(description = "활성화 여부", example = "true")
    Boolean isActive;

    @Schema(description = "등록일", example = "2024-03-19T13:24:02.1008277")
    LocalDateTime createdAt;

}