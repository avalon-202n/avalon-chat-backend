package com.avalon.avalonchat.global.error;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ResponseStatus(BAD_REQUEST)
	@ExceptionHandler({RuntimeException.class})
	public ErrorResponse handleRuntimeException(Exception ex) {
		log.info(ex.getMessage(), ex);
		return new ErrorResponse(BAD_REQUEST, ex);
	}

	@ResponseStatus(INTERNAL_SERVER_ERROR)
	@ExceptionHandler({Exception.class})
	public ErrorResponse handleException(Exception ex) {
		log.error(ex.getMessage(), ex);
		return new ErrorResponse(INTERNAL_SERVER_ERROR, ex);
	}
}
