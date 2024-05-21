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
@ToString(exclude = {"hospital"})
@Comment("병원 권한")
public class HospitalCategory extends BaseTime {

    @Id
    @Column(name = "category_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("병원 권한 순차번호")
    Long seq;

    @Enumerated(EnumType.STRING)
    @Comment("질환 종류")
    Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_seq")
    @Comment("병원 순차번호")
    Hospital hospital;

    protected HospitalCategory(Category category, Hospital hospital) {
        this.category = category;
        this.hospital = hospital;
    }

    public static HospitalCategory create(Category category, Hospital hospital) {
        return new HospitalCategory(category, hospital);
    }
}
