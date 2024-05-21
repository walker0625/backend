package kr.lifesemantics.canofymd.modulecore.domain.sc;

import jakarta.persistence.*;
import kr.lifesemantics.canofymd.modulecore.domain.base.BaseTime;
import kr.lifesemantics.canofymd.modulecore.domain.user.Hospital;
import kr.lifesemantics.canofymd.modulecore.domain.user.Patient;
import kr.lifesemantics.canofymd.modulecore.domain.user.Staff;
import kr.lifesemantics.canofymd.modulecore.enums.Spot;
import kr.lifesemantics.canofymd.modulecore.enums.UserType;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

import javax.security.auth.Subject;

@Entity
@Getter
@DynamicUpdate
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "patient")
@Comment("피부암 진단")
public class Diagnose extends BaseTime {

    @Id
    @Column(name = "diagnose_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("피부암 진단 순차번호")
    Long seq;

    @Comment("진단 그룹 순차번호")
    Long groupSeq;

    @Comment("암 명칭 줄임말")
    String cancerAbbr;

    @Comment("암 명칭 영문")
    String cancerEn;

    @Comment("암 명칭 한글")
    String cancerKo;

    @Comment("암일 확률")
    Double cancerRate;

    @Comment("암이 아닐 확률")
    Double nonCancerRate;

    @Comment("소견")
    String comment;

    @Comment("병변 위치")
    @Enumerated(EnumType.STRING)
    Spot spot;

    @Comment("이미지 키 - uuid")
    String imageKey;

    @Comment("이미지 이름")
    String imageName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_seq")
    @Comment("환자")
    Patient patient;

    @Builder
    public Diagnose(Long seq, Long groupSeq, String cancerAbbr, String cancerEn, String cancerKo, Double cancerRate, Double nonCancerRate, String comment, Spot spot, String imageKey, String imageName, Patient patient) {
        this.seq = seq;
        this.groupSeq = groupSeq;
        this.cancerAbbr = cancerAbbr;
        this.cancerEn = cancerEn;
        this.cancerKo = cancerKo;
        this.cancerRate = cancerRate;
        this.nonCancerRate = nonCancerRate;
        this.comment = comment;
        this.spot = spot;
        this.imageKey = imageKey;
        this.imageName = imageName;
        this.patient = patient;
    }

    public static Diagnose create(Long groupSeq, String cancerAbbr, String cancerEn, String cancerKo, Double cancerRate, Double nonCancerRate,
                                  String comment, Spot spot, String imageKey, String imageName, Patient patient) {
        return Diagnose.builder()
                            .groupSeq(groupSeq)
                            .cancerAbbr(cancerAbbr)
                            .cancerEn(cancerEn)
                            .cancerKo(cancerKo)
                            .cancerRate(cancerRate)
                            .nonCancerRate(nonCancerRate)
                            .comment(comment)
                            .spot(spot)
                            .imageKey(imageKey)
                            .imageName(imageName)
                            .patient(patient)
                       .build();
    }

    public void modifyComment(String comment) {
        this.comment = comment;
    }

}