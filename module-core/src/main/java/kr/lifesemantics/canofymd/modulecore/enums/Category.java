package kr.lifesemantics.canofymd.modulecore.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 * packageName    : kr.lifesemantics.canofymd.modulecore.domain.user.enums
 * fileName       : Category
 * author         : ms.jo
 * date           : 2024/04/18
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/04/18        ms.jo       최초 생성
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public enum Category {

    /**
     * ⭐️️️️️️️️️️ ⭐️️️️️️️️️️ ⭐️️️️️️️️️️ ⭐️️️️️️️️️️ ⭐️️️️️️️️️️ ⭐️️️️️️️️️️ ⭐️️️️️️️️️️ ⭐️️️️️️️️️️
     * if you add category item, you need to create  ⭐️️️️️️️️️️Interceptor ⭐️️️️️️️️️️
     *                                          and rule of beans name is constantName+Interceptor
     *                                                 example ) BPAI + Interceptor = BPAIInterceptor
     *  then, automatically create and mapping with uri and interceptor
     * ⭐️️️️️️️️️️ ⭐️️️️️️️️️️ ⭐️️️️️️️️️️ ⭐️️️️️️️️️️ ⭐️️️️️️️️️️ ⭐️️️️️️️️️️ ⭐️️️️️️️️️️ ⭐️️️️️️️️️️
     */
    BPAI("가정 혈압 예측", "bpai"),
    SCAI("피부암 예측", "scai"),
    ;

    String korean;
    String uri;

    @Override
    public String toString() {
        return toLowerCase();
    }

    public String toLowerCase() {
        return this.name().toLowerCase();
    }

    public String toUpperCase() {
        return this.name();
    }

}
