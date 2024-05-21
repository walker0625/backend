package kr.lifesemantics.canofymd.moduleapi.domain.hospital.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Comment;

@Schema(title = "병원 등록 요청 DTO")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class HospitalRegisterReq {

    @NotBlank(message = "필수 항목입니다.")
    @Schema(description = "병원 이름", example = "메디힐 물리치료")
    String name;

    @Comment("담당자 이름")
    @Schema(description = "담당자 이름", example = "김의사")
    String managerName;

    @Comment("담당자 연락처")
    @Schema(description = "담당자 연락처", example = "010-1111-1111")
    String managerContact;

    @Comment("주소")
    @Schema(description = "주소", example = "서울시 강남구 언주로 533 5층")
    String address;

}
