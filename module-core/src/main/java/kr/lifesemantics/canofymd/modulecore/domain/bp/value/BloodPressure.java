package kr.lifesemantics.canofymd.modulecore.domain.bp.value;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import kr.lifesemantics.canofymd.modulecore.enums.BPStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Comment;

/**
 * packageName    : kr.lifesemantics.canofymd.modulecore.domain.bp.value
 * fileName       : BloodPressure
 * author         : ms.jo
 * date           : 2024/04/23
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/04/23        ms.jo       최초 생성
 */
@Getter
@Embeddable
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BloodPressure {

    @Comment("수축기 혈압")
    int systolic;
    @Comment("이완기 혈압")
    int diastolic;
    @Comment("심박")
    int pulse;

    @Enumerated(EnumType.STRING)
    @Comment("혈압 상태")
    BPStatus status;

    public void setStatus(BPStatus status) {
        this.status = status;
    }

    public BloodPressure(int systolic, int diastolic, int pulse) {
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.pulse = pulse;
    }

    public BloodPressure(int systolic, int diastolic, int pulse, BPStatus status) {
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.pulse = pulse;
        this.status = status;
    }

    static BloodPressure create(int systolic, int diastolic, int pulse) {
        return new BloodPressure(systolic, diastolic, pulse);
    }

}
