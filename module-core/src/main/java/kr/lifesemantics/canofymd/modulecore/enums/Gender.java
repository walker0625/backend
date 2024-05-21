package kr.lifesemantics.canofymd.modulecore.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum Gender {

    MALE("남성", "male"),
    FEMALE("여성", "female"),
    ;

    private String korean;
    private String lower;

}
