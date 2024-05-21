package kr.lifesemantics.canofymd.modulecore.enums;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * packageName    : kr.lifesemantics.canofymd.modulecore.enums
 * fileName       : ParticipationCompliance
 * author         : ms.jo
 * date           : 2024/04/19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/04/19        ms.jo       최초 생성
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public enum ParticipationCompliance {

    WAITING("가입 대기"),
    NORMAL("정상"),
    WARNING("주의"),
    DANGEROUS("위험"),
    ;

    String name;

    @Override
    public String toString() {
        return this.name();
    }
}
