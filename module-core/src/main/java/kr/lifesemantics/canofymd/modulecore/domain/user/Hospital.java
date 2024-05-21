package kr.lifesemantics.canofymd.modulecore.domain.user;

import jakarta.persistence.*;
import kr.lifesemantics.canofymd.modulecore.domain.base.BaseTime;
import kr.lifesemantics.canofymd.modulecore.enums.Category;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

import java.util.ArrayList;

@Entity
@Getter
@DynamicUpdate
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = {"staffs", "patients", "subjects"})
@Comment("병원")
public class Hospital extends BaseTime {

    @Id
    @Column(name = "hospital_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("병원 순차번호")
    Long seq;

    @Comment("이름")
    String name;

    @Comment("담당자 이름")
    String managerName;

    @Comment("담당자 연락처")
    String managerContact;

    @Comment("주소")
    String address;

    @Comment("활성화 여부")
    Boolean isActive;

    @OneToMany(mappedBy = "hospital")
    List<Staff> staffs = new ArrayList<>();

    @OneToMany(mappedBy = "hospital")
    List<Patient> patients = new ArrayList<>();

    @OneToMany(mappedBy = "hospital", cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    List<HospitalCategory> categories = new ArrayList<>();

    @OneToMany(mappedBy = "hospital")
    List<HospitalStatistics> statistics = new ArrayList<>();

    @Builder
    public Hospital(String name, String managerName, String managerContact, String address, Boolean isActive) {
        this.name = name;
        this.managerName = managerName;
        this.managerContact = managerContact;
        this.address = address;
        this.isActive = isActive;
    }

    public static Hospital create(String name, String managerName, String managerContact, String address) {
        Hospital hospital = Hospital.builder()
                                        .name(name)
                                        .managerName(managerName)
                                        .managerContact(managerContact)
                                        .address(address)
                                        .isActive(true)
                                    .build();

        //TODO After receiving a category list as a parameter, create the object and change it to the next version by putting it in the list
        for (Category category : Category.values()) {
            hospital.getCategories().add(HospitalCategory.create(category, hospital));
        }


        return hospital;
    }

    public static Hospital modify(String name, String managerName, String managerContact, String address) {
        return Hospital.builder()
                .name(name)
                .managerName(managerName)
                .managerContact(managerContact)
                .address(address)
                .isActive(true)
                .build();
    }


    public void modify(String name, String managerName, String managerContact, String address, Boolean isActive) {
        this.name = name;
        this.managerName = managerName;
        this.managerContact = managerContact;
        this.address = address;
        this.isActive = isActive;
    }

}