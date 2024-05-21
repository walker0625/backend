package kr.lifesemantics.canofymd.moduleapi.domain.hospital.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Comment;

@Schema(title = "병원 수정 요청 DTO")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class HospitalModifyReq {

    @Schema(description = "병원 순차번호", example = "1")
    Long hospitalSeq;

    @Schema(description = "병원 이름", example = "라이프병원")
    String name;

    @Schema(description = "담당자 이름", example = "김의사")
    String managerName;

    @Schema(description = "담당자 연락처", example = "010-1111-1111")
    String managerContact;

    @Schema(description = "주소", example = "서울시 강남구 언주로 533 5층")
    String address;

    @Schema(description = "병원 활성화 여부", type = "Boolean", allowableValues = {"true", "false"}, example = "true")
    Boolean isActive;

}