package kr.lifesemantics.canofymd.moduleapi.domain.bp.history.vo;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.bp.history.vo
 * fileName       : PeriodVO
 * author         : ms.jo
 * date           : 2024/05/08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/05/08        ms.jo       최초 생성
 */

public record PeriodVO(LocalDate from, LocalDate to) {

    public boolean isContains(LocalDate input) {
        return to.compareTo(input) >= 0 && from.compareTo(input) <= 0;
    }

}
