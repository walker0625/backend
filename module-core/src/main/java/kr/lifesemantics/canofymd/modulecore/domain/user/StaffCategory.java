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
@ToString(exclude = {"staff"})
@Comment("질환 권한")
public class StaffCategory extends BaseTime {

    @Id
    @Column(name = "category_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("질환 권한 순차번호")
    Long seq;

    @Enumerated(EnumType.STRING)
    @Comment("질환 종류")
    Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_seq")
    Staff staff;

    protected StaffCategory(Category category, Staff staff) {
        this.category = category;
        this.staff = staff;
    }

    public static StaffCategory of(Category category, Staff staff) { return new StaffCategory(category, staff); }
}
