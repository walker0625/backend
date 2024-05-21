package kr.lifesemantics.canofymd.moduleapi.domain.staff.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Transient;
import kr.lifesemantics.canofymd.modulecore.domain.user.Staff;
import kr.lifesemantics.canofymd.modulecore.enums.Category;
import kr.lifesemantics.canofymd.modulecore.enums.UserType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(title = "병원관계자 등록 요청 DTO")
public class StaffRegisterReq {

    @Schema(description = "아이디", example = "doctor1")
    String id;

    @JsonIgnore
    @Schema(description = "자동 생성되는 암호화된 비밀번호(원문 : DefaultValues.DEFAULT_PASSWORD 참조)", example = "")
    String password;

    @Schema(description = "이름", example = "김의사")
    String name;

    @Schema(description = "사용자 구분", example = "DOCTOR")
    UserType userType;

    @Schema(description = "연락처", example = "010-1111-1111")
    String contact;

    @Schema(description = "병원 순차번호", example = "1")
    Long hospitalSeq;

    @Schema(description = "카테고리", examples = "[\"BPAI\"]")
    List<Category> category;

}