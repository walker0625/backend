package kr.lifesemantics.canofymd.modulecore.domain.user;

import jakarta.persistence.*;
import kr.lifesemantics.canofymd.modulecore.enums.Category;
import kr.lifesemantics.canofymd.modulecore.enums.UserType;
import kr.lifesemantics.canofymd.modulecore.domain.base.BaseTime;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DynamicUpdate
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"hospital", "categories"})
@Comment("병원관계자")
public class Staff extends BaseTime {

    @Id
    @Column(name = "staff_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("병원관계자 순차번호")
    Long seq;

    @Comment("ID")
    @Column(length = 20)
    String id;

    @Comment("비밀번호")
    String password;

    @Comment("이름")
    @Column(length = 20)
    String name;

    @Comment("연락처")
    @Column(length = 20)
    String contact;

    @Enumerated(value = EnumType.STRING)
    @Comment("병원관계자 타입")
    @Column(length = 20)
    UserType userType;

    @Comment("사용자 활성화 여부")
    boolean isActive;

    @Comment("패스워드 변경 여부")
    boolean isPasswordChange;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_seq")
    @Comment("소속 병원 순차번호")
    Hospital hospital;

    @OneToMany(mappedBy = "staff", cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    List<StaffCategory> categories = new ArrayList<>();

    @Builder
    public Staff(Long seq, String id, String password, String name, String contact, UserType userType, boolean isActive, boolean isPasswordChange, Hospital hospital) {
        this.seq = seq;
        this.id = id;
        this.password = password;
        this.name = name;
        this.contact = contact;
        this.userType = userType;
        this.isActive = isActive;
        this.isPasswordChange = isPasswordChange;
        this.hospital = hospital;
    }

    public static Staff create(String id, String password, String name, String contact, UserType userType, Hospital hospital, List<Category> category) {
        Staff staff = Staff.builder()
                .id(id)
                .password(password)
                .name(name)
                .contact(contact)
                .userType(userType)
                .isActive(true)
                .isPasswordChange(false)
                .hospital(hospital)
                .build();


        if(userType != UserType.ADMIN) {
            category.forEach(c -> {
               staff.getCategories().add(StaffCategory.of(c, staff));
            });
        }

        return staff;
    }

    public void modify(String name, String contact, UserType userType, Boolean isActive) {
        this.name = name;
        this.contact = contact;
        this.userType = userType;
        this.isActive = isActive;
    }

    public void passwordModify(String password) {
        this.password = password;
        this.isPasswordChange = true;
    }

}