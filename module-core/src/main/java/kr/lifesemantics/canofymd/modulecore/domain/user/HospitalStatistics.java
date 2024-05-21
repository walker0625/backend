package kr.lifesemantics.canofymd.modulecore.domain.user;

import jakarta.persistence.*;
import kr.lifesemantics.canofymd.modulecore.domain.base.BaseTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

/**
 * packageName    : kr.lifesemantics.canofymd.modulecore.domain.user
 * fileName       : HospitalStatistics
 * author         : ms.jo
 * date           : 2024/04/25
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/04/25        ms.jo       최초 생성
 */

@Entity
@Getter
@DynamicUpdate
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Comment("병원 일별 통계")
public class HospitalStatistics extends BaseTime {

    @Id
    @Column(name = "hospital_statistics_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("병원 통계 순차번호")
    Long seq;

    @Comment("총 가입 이용자")
    Long totalRegisterCount = 0L;

    @Comment("당일 측정한 이용자")
    Long measuredUserCount = 0L;

    @Comment("누적 예측자")
    Long totalPredictedUser = 0L;

    @Comment("기준일")
    LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hospital_seq")
    @Comment("병원 순차번호")
    Hospital hospital;

    @PrePersist
    protected void onCreate() {
        date = LocalDate.now();
    }

}
