package kr.lifesemantics.canofymd.moduleapi.webClient.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.webClient.dto.req
 * fileName       : PredictionReq
 * author         : ms.jo
 * date           : 2024/05/03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/05/03        ms.jo       최초 생성
 */
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PredictionReq {

    String registration;

    List<Double> sbp;

    List<Double> dbp;

    List<Double> pulse;

    List<String> date;

}
