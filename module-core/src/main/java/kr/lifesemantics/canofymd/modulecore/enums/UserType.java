package kr.lifesemantics.canofymd.modulecore.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public enum UserType {

	ADMIN("ADMIN", 1, "관리자"),
	DOCTOR("DOCTOR", 2, "의사"),
	COORDINATOR("COORDINATOR", 3, "코디네이터"),
	PATIENT("PATIENT", 3, "환자"),
	;

	private String roleName;
	private int code;
	private String korean;

	public List<String> getRoles() {
		return List.of(this.getRoleName());
	}

}