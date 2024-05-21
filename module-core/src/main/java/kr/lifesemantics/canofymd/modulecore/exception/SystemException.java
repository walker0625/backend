package kr.lifesemantics.canofymd.modulecore.exception;

import kr.lifesemantics.canofymd.modulecore.util.ResponseStatus;
import lombok.Getter;
import kr.lifesemantics.canofymd.modulecore.util.ResponseStatus;

// 비정상적인 System 예외 발생시 사용(httpStatus - 500)
@Getter
public class SystemException extends RuntimeException {

	private final ResponseStatus responseStatus;

	public SystemException(ResponseStatus responseStatus) {
		this.responseStatus = responseStatus;
	}

}
