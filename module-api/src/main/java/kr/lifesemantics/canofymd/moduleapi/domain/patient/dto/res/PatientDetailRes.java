package kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Transient;
import kr.lifesemantics.canofymd.modulecore.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.patient.dto.res
 * fileName       : PatientDetailRes
 * author         : ms.jo
 * date           : 2024/04/25
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/04/25        ms.jo       최초 생성
 */

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(title = "환자 조회 결과 DTO")
public class PatientDetailRes {

    @Schema(description = "환자 순차번호", example = "1")
    Long seq;

    @Schema(description = "환자 아이디", example = "patient1")
    String id;

    @Schema(description = "환자 이름", example = "김환자")
    String name;

    @Schema(description = "연락처", example = "010-1111-2222")
    String contact;

    @Schema(description = "성별", example = "MALE")
    Gender gender;

    @Schema(description = "생년월일", example = "1995-02-27")
    LocalDate birthDate;

    @Schema(description = "가입일", example = "2024-04-24")
    LocalDate createdDate;

    @Schema(description = "키", example = "177.7")
    double height;

    @Schema(description = "체중", example = "77.7")
    double weight;

    @Schema(description = "음주 여부", example = "true")
    boolean isDrink;

    @Schema(description = "흡연 여부", example = "false")
    boolean isSmoke;

    @Schema(description = "가족력 여부", example = "false")
    boolean isFamilyDisease;

    @Transient
    @JsonIgnore
    LocalDateTime createdAt;

    @Builder
    public PatientDetailRes(Long seq, String id, String name, String contact, Gender gender, LocalDate birthDate,
                            double height, double weight, boolean isDrink, boolean isSmoke, boolean isFamilyDisease, LocalDateTime createdAt) {
        this.seq = seq;
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.gender = gender;
        this.birthDate = birthDate;
        this.height = height;
        this.weight = weight;
        this.isDrink = isDrink;
        this.isSmoke = isSmoke;
        this.isFamilyDisease = isFamilyDisease;
        this.createdAt = createdAt;
        this.createdDate = createdAt.toLocalDate();
    }
}
