package kr.lifesemantics.canofymd.modulecore.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * packageName    : kr.lifesemantics.canofymd.web.global.util
 * fileName       : TimeUtil
 * author         : ms.jo
 * date           : 2024/03/26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/03/26        ms.jo       최초 생성
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TimeUtil {

    /**
     * @param start
     * @param end
     * @return 두 시간의 차이를 LocalTime 타입으로 반환
     */
    public static LocalTime getTimeDifferenceAsLocalTime(LocalDateTime start, LocalDateTime end) {
            Duration duration = Duration.between(start, end);
            long seconds = duration.getSeconds();
            int hours = (int) seconds / 3600;
            int minutes = (int) (seconds % 3600) / 60;
            int secs = (int) seconds % 60;
            return LocalTime.of(hours, minutes, secs);
        }

}
