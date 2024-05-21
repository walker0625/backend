package kr.lifesemantics.canofymd.modulecore.enums;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * packageName    : kr.lifesemantics.canofymd.modulecore.enums
 * fileName       : MedicationControl
 * author         : ms.jo
 * date           : 2024/04/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/04/24        ms.jo       최초 생성
 */

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public enum MedicationControl {

    INCREASE("증가"),
    STAY("유지"),
    DECREASE("감소"),
    CAN_NOT_JUDGE("판단불가"),
    ;

    String korean;
}
