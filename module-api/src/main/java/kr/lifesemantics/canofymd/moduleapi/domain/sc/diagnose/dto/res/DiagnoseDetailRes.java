package kr.lifesemantics.canofymd.moduleapi.domain.sc.diagnose.dto.res;

import kr.lifesemantics.canofymd.modulecore.enums.Gender;
import kr.lifesemantics.canofymd.modulecore.enums.Spot;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DiagnoseDetailRes {

    // 환자 정보
    String name;
    LocalDate birthDate;
    Gender gender;
    String contact;

    // 진단 정보
    Long diagnoseSeq;
    Spot spot;
    String comment;
    double cancerRate;
    double nonCancerRate;
    String cancerAbbr;
    String cancerEn;
    String cancerKo;
    LocalDateTime diagnoseAt;

    String mainImageUrl;

    List<DiagnoseImage> previousImageList;

    @Builder
    public DiagnoseDetailRes(String name, LocalDate birthDate, Gender gender, String contact, Long diagnoseSeq, Spot spot, String comment,
                             double cancerRate, double nonCancerRate, String cancerAbbr, String cancerEn, String cancerKo, LocalDateTime diagnoseAt,
                             String mainImageUrl, List<DiagnoseImage> previousImageList) {
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.contact = contact;
        this.diagnoseSeq = diagnoseSeq;
        this.spot = spot;
        this.comment = comment;
        this.cancerRate = cancerRate;
        this.nonCancerRate = nonCancerRate;
        this.cancerAbbr = cancerAbbr;
        this.cancerEn = cancerEn;
        this.cancerKo = cancerKo;
        this.diagnoseAt = diagnoseAt;
        this.mainImageUrl = mainImageUrl;
        this.previousImageList = previousImageList;
    }

    public static DiagnoseDetailRes res(String name, LocalDate birthDate, Gender gender, String contact, Long diagnoseSeq, Spot spot, String comment,
                                        double cancerRate, double nonCancerRate, String cancerAbbr, String cancerEn, String cancerKo, LocalDateTime diagnoseAt,
                                        String mainImageUrl, List<DiagnoseImage> previousImageList) {
        return DiagnoseDetailRes.builder()
                .name(name)
                .birthDate(birthDate)
                .gender(gender)
                .contact(contact)
                .diagnoseSeq(diagnoseSeq)
                .spot(spot)
                .comment(comment)
                .cancerRate(cancerRate)
                .nonCancerRate(nonCancerRate)
                .cancerAbbr(cancerAbbr)
                .cancerEn(cancerEn)
                .cancerKo(cancerKo)
                .diagnoseAt(diagnoseAt)
                .mainImageUrl(mainImageUrl)
                .previousImageList(previousImageList)
                .build();
    }

}
