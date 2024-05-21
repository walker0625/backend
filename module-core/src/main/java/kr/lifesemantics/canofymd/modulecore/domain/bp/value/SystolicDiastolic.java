package kr.lifesemantics.canofymd.modulecore.domain.bp.value;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Comment;

/**
 * packageName    : kr.lifesemantics.canofymd.modulecore.domain.bp.value
 * fileName       : SystolicDiastolic
 * author         : ms.jo
 * date           : 2024/05/03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/05/03        ms.jo       최초 생성
 */
@Getter
@Embeddable
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SystolicDiastolic {

    @Comment("수축기 혈압")
    double systolic;
    @Comment("이완기 혈압")
    double diastolic;

    public SystolicDiastolic(double systolic, double diastolic) {
        this.systolic = systolic;
        this.diastolic = diastolic;
    }

    static SystolicDiastolic create(double systolic, double diastolic) {
        return new SystolicDiastolic(systolic, diastolic);
    }
}
