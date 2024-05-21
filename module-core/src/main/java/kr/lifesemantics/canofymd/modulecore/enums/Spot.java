package kr.lifesemantics.canofymd.modulecore.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum Spot {

    HEADNECK("머리목", "head&neck"),
    EXTREMITY("팔다리", "extremity"),
    TRUNK("몸통","trunk"),
    ACRAL("손발","acral")
    ;

    private String korean;
    private String lower;

}
