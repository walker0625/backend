package kr.lifesemantics.canofymd.modulecore.domain.bp;

import jakarta.persistence.*;
import kr.lifesemantics.canofymd.modulecore.domain.base.BaseTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

/**
 * packageName    : kr.lifesemantics.canofymd.modulecore.domain.bp
 * fileName       : BpSummaryLog
 * author         : ms.jo
 * date           : 2024/04/23
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/04/23        ms.jo       최초 생성
 */
@Entity
@Getter
@DynamicUpdate
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "patient")
@Comment("혈압 통계 로그")
public class BpSummaryLog extends BaseTime {

    @Id
    @Column(name = "bp_summary_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("혈압 통계 로그 순차번호")
    Long seq;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "history_seq")
    @Comment("혈압 기록 순차번호")
    BpHistory history;

    @Comment("성공 여부")
    boolean isSuccess;

    @Comment("로그")
    String log;

}
