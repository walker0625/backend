package kr.lifesemantics.canofymd.moduleapi.domain.hospital.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Schema(title = "병원 상세 조회 DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HospitalDetailRes {

    @Schema(description = "병원 순차번호", example = "1")
    Long seq;

    @Schema(description = "병원명", example = "메디힐 의원")
    String name;

    @Schema(description = "담당자 이름", example = "김의사")
    String managerName;

    @Schema(description = "담당자 연락처", example = "010-1111-1111")
    String managerContact;

    @Schema(description = "병원 연락처", example = "02-445-5443")
    String contact;

    @Schema(description = "주소", example = "서울시 강남구 언주로 533 5층")
    String address;

    @Schema(description = "운영 여부", example = "true", type = "Boolean", allowableValues = {"true", "false"})
    Boolean isActive;

    @Schema(description = "등록일", example = "2024-04-14")
    LocalDate registerDate;

}
