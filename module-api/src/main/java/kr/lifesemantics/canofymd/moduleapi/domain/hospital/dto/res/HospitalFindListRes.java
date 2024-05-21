package kr.lifesemantics.canofymd.moduleapi.domain.hospital.dto.res;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(title = "병원 조회 결과 DTO")
public class HospitalFindListRes {

    @Schema(description = "병원 순차번호", example = "1")
    Long seq;

    @Schema(description = "병원명", example = "메디힐 의원")
    String name;

    @Schema(description = "담당자 이름", example = "김의사")
    String managerName;

    @Schema(description = "담당자 연락처", example = "010-1111-1111")
    String managerContact;

    @Schema(description = "주소", example = "서울시 강남구 언주로 533 5층")
    String address;

    @JsonIgnore
    @Transient
    LocalDateTime createdAt;

    @Schema(description = "등록일", example = "2023-03-11")
    LocalDate createdDate;

    @Schema(description = "운영 여부", example = "true", type = "Boolean", allowableValues = {"true", "false"})
    Boolean isActive;

    @Builder
    @QueryProjection
    public HospitalFindListRes(Long seq, String name, String managerName, String managerContact, String address, LocalDateTime createdAt, Boolean isActive) {
        this.seq = seq;
        this.name = name;
        this.managerName = managerName;
        this.managerContact = managerContact;
        this.address = address;
        this.createdAt = createdAt;
        this.isActive = isActive;

        this.createdDate = createdAt.toLocalDate();
    }

}