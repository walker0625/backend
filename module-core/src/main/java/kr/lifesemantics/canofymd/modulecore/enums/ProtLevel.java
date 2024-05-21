package kr.lifesemantics.canofymd.modulecore.enums;

import lombok.ToString;

/**
 * packageName    : kr.lifesemantics.canofymd.modulecore.enums
 * fileName       : ProtLevel
 * author         : ms.jo
 * date           : 2024/04/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/04/26        ms.jo       최초 생성
 */
public enum ProtLevel {

    NEGATIVE,
    TRACE,
    ONE_PLUS,
    TWO_PLUS,
    THREE_PLUS,
    FOUR_PLUS,
    ;

    @Override
    public String toString() {
        return this.name();
    }
}
