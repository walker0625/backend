package kr.lifesemantics.canofymd.modulecore.enums;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * packageName    : kr.lifesemantics.canofymd.modulecore.enums
 * fileName       : CardiovascularDiseaseRisk
 * author         : ms.jo
 * date           : 2024/04/24
 * description    : 심혈관계질환 위험도
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024/04/24        ms.jo       최초 생성
 */

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public enum CardiovascularDiseaseRisk {

    NORMAL(1, 135, 85),
    DANGEROUS(2, 130, 80),
    CAN_NOT_JUDGE(3, -1, -1),
    ;

    int level;
    int goalSystolic;
    int goalDiastolic;



}
