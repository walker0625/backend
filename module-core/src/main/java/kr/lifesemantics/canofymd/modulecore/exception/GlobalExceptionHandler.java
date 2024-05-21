package kr.lifesemantics.canofymd.modulecore.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.lifesemantics.canofymd.modulecore.util.ResponseStatus;
import kr.lifesemantics.canofymd.modulecore.util.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Component
//@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ResponseBody
	@ExceptionHandler(BusinessException.class)
	protected ResponseEntity<Object> handleBusinessException(BusinessException exception) {

		log.info("business exception! :: {}", exception.toString());

		Map<String, Object> body = exception.getResponseStatus().getBody();

		return new ResponseEntity<>(
				body == null ? exception.getResponseStatus().toString()
							 : exception.getResponseStatus().toString(body),
				exception.getHttpStatus()); // httpStatus
	}

	@ResponseBody
	@ExceptionHandler(SystemException.class)
	protected ResponseEntity<Object> handleSystemException(SystemException exception) {

		log.error("SystemException", exception);
		
		return new ResponseEntity<>(exception.getResponseStatus().toString(), // body
									HttpStatus.INTERNAL_SERVER_ERROR); // httpStatus : 500
	}

	@ResponseBody
	@ExceptionHandler(JsonProcessingException.class)
	protected ResponseEntity<Object> handleJsonProcessingException(JsonProcessingException exception) {

		log.error("SystemException", exception);

		return new ResponseEntity<>(ResponseStatus.OCCURRED_ON_PARSING.getMessage(), // body
									HttpStatus.INTERNAL_SERVER_ERROR); // httpStatus : 500
	}

	@ResponseBody
	@ExceptionHandler(HttpMessageNotReadableException.class)
	protected ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {

		log.error("SystemException", exception);

		return new ResponseEntity<>(ResponseStatus.CHECK_DATE_FORMAT.getMessage(), // body
									HttpStatus.BAD_REQUEST); // httpStatus : 500
	}

/*

	@ExceptionHandler(PageException.class)
	protected String handlePageException(PageException exception) {

		log.error("PageException", exception);

		return returnErrorPage(exception);
	}

	// 의도치 않은 그외 Exception 처리
	@ExceptionHandler(Exception.class)
	protected String handleException(Exception exception) {

		log.error("Exception", exception);
		log.error("normal here", exception);

		return returnErrorPage(exception);
	}

	private String returnErrorPage(Exception exception) {

		String returnErrorPage = "/error/500"; // 기본 에러페이지는 500

		String exceptionName = exception.getClass().getSimpleName();

		returnErrorPage = switch (exceptionName) {
			case "AccessDeniedException" -> "/error/403";
			case "NotFoundException" -> "/error/404";
			default -> returnErrorPage;
		};

		return returnErrorPage;
	}
*/

}