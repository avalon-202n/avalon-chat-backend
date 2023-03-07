package com.avalon.avalonchat.global.error;

import static org.apache.commons.lang3.ObjectUtils.*;
import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ResponseStatus(BAD_REQUEST)
	@ExceptionHandler(RuntimeException.class)
	public ErrorResponse handleRuntimeException(Exception ex) {
		log.info(messageIfNull(ex.getMessage()), ex);
		return new ErrorResponse(BAD_REQUEST, ex);
	}

	@ResponseStatus(NOT_FOUND)
	@ExceptionHandler(NoHandlerFoundException.class)
	public ErrorResponse handleNoHandlerFoundException(Exception ex) {
		return new ErrorResponse(NOT_FOUND, ex);
	}

	@ResponseStatus(INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ErrorResponse handleException(Exception ex) {
		log.error(messageIfNull(ex.getMessage()), ex);
		return new ErrorResponse(INTERNAL_SERVER_ERROR, ex);
	}

	private String messageIfNull(String message) {
		return defaultIfNull(message, "exception with no message");
	}
}
