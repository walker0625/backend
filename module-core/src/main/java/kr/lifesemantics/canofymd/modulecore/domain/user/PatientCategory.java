package kr.lifesemantics.canofymd.modulecore.domain.user;

import jakarta.persistence.*;
import kr.lifesemantics.canofymd.modulecore.domain.base.BaseTime;
import kr.lifesemantics.canofymd.modulecore.enums.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@DynamicUpdate
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"patient"})
@Comment("환자 권한")
public class PatientCategory extends BaseTime {

    @Id
    @Column(name = "category_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("환자 권한 순차번호")
    Long seq;

    @Enumerated(EnumType.STRING)
    @Comment("질환 종류")
    Category category;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_seq")
    @Comment("환자 순차번호")
    Patient patient;

    protected PatientCategory(Category category, Patient patient) {
        this.category = category;
        this.patient = patient;
    }

    public static PatientCategory create(Category category, Patient patient) {
        return new PatientCategory(category, patient);
    }
}
