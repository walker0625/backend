package kr.lifesemantics.canofymd.moduleapi.webClient.dto.res;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.webClient.dto.res
 * fileName       : PredictionAmount
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
public class PredictionAmount {

    /*List<Double> measuredSystolic;

    List<Double> measuredDiastolic;*/

    List<Double> predictedSystolic;

    List<Double> predictedDiastolic;

}
