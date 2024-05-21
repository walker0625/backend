package kr.lifesemantics.canofymd.modulecore.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public enum AuthType {

	STAFF("STAFF", 1, "병원관계자"),
	PATIENT("PATIENT", 2, "환자"), // BP
	;

	private String roleName;
	private int code;
	private String korean;

	public List<String> getRoles() {
		return List.of(this.getRoleName());
	}

}