package kr.lifesemantics.canofymd.moduleapi.domain.bp.prediction.service;

import kr.lifesemantics.canofymd.moduleapi.domain.bp.prediction.dto.res.PredictionDetailRes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/**
 * packageName    : kr.lifesemantics.canofymd.moduleapi.domain.bp.prediction.service
 * fileName       : PredictionServiceTest
 * author         : ms.jo
 * date           : 2024/05/03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/05/03        ms.jo       최초 생성
 */
/* TODO 빌드 속도를 위해 사용시 해제
@ActiveProfiles("local")
@SpringBootTest
@Transactional(readOnly = false)
class PredictionServiceTest {

    @Autowired
    private PredictionService predictionService;

    @Test
    public void 예측_요청이_올바르게_간다() throws Exception {
    
        //given
        try {
            PredictionDetailRes predict = predictionService.predict(1L);
        }
        catch (RuntimeException e) {

        }

        //when
        
        //then
    
    }
    

}
 */