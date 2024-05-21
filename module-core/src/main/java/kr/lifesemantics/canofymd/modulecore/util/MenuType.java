package kr.lifesemantics.canofymd.modulecore.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : kr.lifesemantics.canofymd.web.global.util
 * fileName       : MenuType
 * author         : ms.jo
 * date           : 2024/03/20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/03/20        ms.jo       최초 생성
 */
@AllArgsConstructor
@Getter
public enum MenuType {

    DASHBOARD("dashboard"),
    SUBJECTS("subjects"),
    VISITS("visits"),
    COORDINATORS("coordinators"),
    CENTERS("centers"),
    ;

    private String name;

}
