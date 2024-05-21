package kr.lifesemantics.canofymd.modulecore.exception;

import kr.lifesemantics.canofymd.modulecore.util.ResponseStatus;
import kr.lifesemantics.canofymd.modulecore.util.ResponseStatus;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

// 처리가능한 Business 프로세스 상의 예외 발생시 사용(httpStatus - 상황에 따라 설정)
@Getter
@ToString
public class BusinessException extends RuntimeException {

	private final ResponseStatus responseStatus;
	private HttpStatus httpStatus;
	
	public BusinessException(ResponseStatus responseStatus) {
		this.responseStatus = responseStatus;
		this.httpStatus = HttpStatus.OK;
	}

	public BusinessException(ResponseStatus responseStatus, HttpStatus httpStatus) {
		this.responseStatus = responseStatus;
		this.httpStatus = httpStatus;
	}

}
