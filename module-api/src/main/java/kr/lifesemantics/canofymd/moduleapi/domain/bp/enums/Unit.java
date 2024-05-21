package kr.lifesemantics.canofymd.moduleapi.domain.bp.enums;

import lombok.Getter;
import lombok.ToString;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.bp.enums
 * fileName       : Unit
 * author         : ms.jo
 * date           : 2024/05/08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/05/08        ms.jo       최초 생성
 */

@Getter
public enum Unit {
    WEEK,
    MONTH
    ;

    @Override
    public String toString() {
        return this.name();
    }
}
