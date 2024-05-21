package kr.lifesemantics.canofymd.moduleapi.domain.bp.history.mapper;

import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.res.BpHistoryDetailRes;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.res.BpHistoryLatestRes;
import kr.lifesemantics.canofymd.moduleapi.domain.bp.history.dto.res.BpHistoryRegisterRes;
import kr.lifesemantics.canofymd.moduleapi.global.util.StatisticsUtil;
import kr.lifesemantics.canofymd.modulecore.domain.bp.BpHistory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BpHistoryMapper {

    public BpHistoryRegisterRes toBpHistoryRegisterRes(BpHistory bpHistory) {
        return BpHistoryRegisterRes.builder()
                                        .seq(bpHistory.getSeq())
                                        .patientSeq(bpHistory.getPatient().getSeq())
                                        .firstBP(StatisticsUtil.judgeBPStatus(bpHistory.getFirstBP()))
                                        .secondBP(StatisticsUtil.judgeBPStatus(bpHistory.getSecondBP()))
                                        .week(bpHistory.getWeek())
                                        .measureDate(bpHistory.getMeasureDate())
                                        .measureTime(bpHistory.getMeasureTime())
                                        .isUsable(bpHistory.isUsable())
                                    .build();
    }

    public BpHistoryLatestRes toBpHistoryLatestRes(BpHistory latestBp) {
        return BpHistoryLatestRes.builder()
                                    .latestBp(latestBp.getSecondBP())
                                    .measureTime(latestBp.getMeasureTime())
                                 .build();
    }

    public BpHistoryDetailRes toBpHistoryDetailRes(BpHistory bpHistory) {

        return BpHistoryDetailRes.builder()
                                    .seq(bpHistory.getSeq())
                                    .firstBP(StatisticsUtil.judgeBPStatus(bpHistory.getFirstBP()))
                                    .secondBP(StatisticsUtil.judgeBPStatus(bpHistory.getSecondBP()))
                                    .measureTime(bpHistory.getMeasureTime())
                                 .build();
    }
}
