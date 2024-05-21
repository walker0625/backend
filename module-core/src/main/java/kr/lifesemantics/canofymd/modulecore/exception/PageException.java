package kr.lifesemantics.canofymd.modulecore.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

// 에러페이지를 리턴하고 싶을 때 사용(httpStatus - 403 / 404 / 500)
@Getter
public class PageException extends RuntimeException {

    private HttpStatus httpStatus;

    public PageException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

}
