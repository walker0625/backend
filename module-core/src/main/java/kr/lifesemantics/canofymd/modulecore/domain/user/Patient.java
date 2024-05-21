package kr.lifesemantics.canofymd.modulecore.domain.user;

import jakarta.persistence.*;
import kr.lifesemantics.canofymd.modulecore.domain.base.BaseTime;
import kr.lifesemantics.canofymd.modulecore.domain.bp.BpHistory;
import kr.lifesemantics.canofymd.modulecore.domain.bp.Treatment;
import kr.lifesemantics.canofymd.modulecore.enums.Category;
import kr.lifesemantics.canofymd.modulecore.enums.Gender;
import kr.lifesemantics.canofymd.modulecore.enums.ParticipationCompliance;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DynamicUpdate
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "hospital")
@Comment("환자")
public class Patient extends BaseTime {

    @Id
    @Column(name = "patient_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("환자 순차번호")
    Long seq;

    @Comment("ID")
    String id;

    @Comment("비밀번호")
    String password;

    @Comment("이름")
    String name;

    @Comment("연락처")
    String contact;

    @Enumerated(EnumType.STRING)
    @Comment("성별")
    Gender gender;

    @Comment("첫측정일")
    LocalDate firstMeasuredDate = null;

    @Comment("마지막 측정일")
    LocalDate lastMeasuredDate = null;

    @Comment("생년월일")
    LocalDate birthDate;

    @Comment("체중(kg)")
    double weight;

    @Comment("신장(cm)")
    double height;

    @Comment("FCM push 토큰")
    String pushToken;

    @Comment("흡연 여부")
    boolean isSmoke;

    @Comment("음주 여부")
    boolean isDrink;

    @Comment("가족력 여부")
    boolean isFamilyDisease;

    @Comment("패스워드 변경 여부 (false -> 변경 필요")
    boolean isPasswordChange = false;

    @Comment("현재 주차")
    long week = 0L;

    @Comment("활성화 여부")
    boolean isActive = true;

    @Comment("측정 순응도 상태")
    @Enumerated(EnumType.STRING)
    ParticipationCompliance status = ParticipationCompliance.WAITING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_seq")
    @Comment("등록 센터 순차번호")
    Hospital hospital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_seq")
    @Comment("담당 의사 순차번호")
    Staff staff;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.PERSIST)
    List<BpHistory> bpHistories = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.PERSIST)
    List<Treatment> treatments = new ArrayList<>();

    @OneToOne(mappedBy = "patient", cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    PatientCategory category;

    public void overWeek() {
        week++;
    }

    public int getAge() {
        return birthDate.until(LocalDate.now()).getYears();
    }

    public double getBMI() {
        if(weight < 1 || height < 1) {
            return 0;
        }
        else {
            return weight / (height * height / 10000);
        }
    }

    @Builder
    private Patient(String id, String password, String name, String contact, Gender gender, LocalDate birthDate,
                   double weight, double height, String pushToken, boolean isSmoke, boolean isDrink, boolean isFamilyDisease, boolean isPasswordChange, long week, boolean isActive,
                   Hospital hospital, Staff staff) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.contact = contact;
        this.gender = gender;
        this.birthDate = birthDate;
        this.firstMeasuredDate = null;
        this.lastMeasuredDate = null;
        this.weight = weight;
        this.height = height;
        this.pushToken = pushToken;
        this.isSmoke = isSmoke;
        this.isDrink = isDrink;
        this.isFamilyDisease = isFamilyDisease;
        this.isPasswordChange = isPasswordChange;
        this.week = week;
        this.isActive = isActive;
        this.hospital = hospital;
        this.status = ParticipationCompliance.WAITING;
        this.staff = staff;

    }

    public static Patient create(String id, String password, String name, String contact, Gender gender, LocalDate birthDate,
                                 double weight, double height, boolean isSmoke, boolean isDrink, boolean isFamilyDisease, Category category, Hospital hospital, Staff staff) {

        Patient patient = Patient.builder()
                .id(id)
                .password(password)
                .name(name)
                .contact(contact)
                .gender(gender)
                .birthDate(birthDate)
                .weight(weight)
                .height(height)
                //TODO add logic that generate push token, fcm
                .pushToken(null)
                .isSmoke(isSmoke)
                .isDrink(isDrink)
                .isFamilyDisease(isFamilyDisease)
                .isActive(true)
                .hospital(hospital)
                .staff(staff)
                .build();
        hospital.getPatients().add(patient);
        patient.categoryUpdate(category);


        return patient;
    }

    public void categoryUpdate(Category category) {
        this.category = PatientCategory.create(category, this);
    }

    public void modify(String name, String contact, Gender gender, LocalDate birthDate,
                                     double weight, double height, boolean isSmoke, boolean isDrink, boolean isFamilyDisease) {

        this.name = name;
        this.contact = contact;
        this.gender = gender;
        this.birthDate = birthDate;
        this.weight = weight;
        this.height = height;
        this.isSmoke = isSmoke;
        this.isDrink = isDrink;
        this.isFamilyDisease = isFamilyDisease;

    }

    public void updatePushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public void changePassword(String password) {
        this.password = password;
        this.isPasswordChange = true;
    }

    public void resetPassword(String password) {
        this.password = password;
        this.isPasswordChange = false;
    }

    public void changeMeasureStatus(ParticipationCompliance status) {
        if(status == ParticipationCompliance.WAITING) {
            if(firstMeasuredDate == null) {
                this.status = ParticipationCompliance.WAITING;
            }
            else {
                this.status = ParticipationCompliance.DANGEROUS;
            }
        }
        else {
            this.status = status;
        }
    }

    public void changeLastMeasuredDate(LocalDate lastMeasuredDate) {
        this.lastMeasuredDate = lastMeasuredDate;
    }

    public void changeFirstMeasuredDate(LocalDate measuredDate) {
            this.firstMeasuredDate = measuredDate;
    }

    public void changeStatus(ParticipationCompliance status) {
        this.status = status;
    }

}

