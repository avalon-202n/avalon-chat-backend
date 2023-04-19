package com.avalon.avalonchat.global.error;

import static org.springframework.http.HttpStatus.*;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.avalon.avalonchat.global.error.exception.AvalonChatRuntimeException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	private final MessageSourceAccessor messageSourceAccessor;

	public GlobalExceptionHandler(MessageSource messageSource) {
		this.messageSourceAccessor = new MessageSourceAccessor(messageSource);
	}

	@ResponseStatus(BAD_REQUEST)
	@ExceptionHandler(AvalonChatRuntimeException.class)
	public ErrorResponse handleAvalonChatRuntimeException(AvalonChatRuntimeException ex) {
		String type = ex.getClass().getSimpleName();
		String message = messageSourceAccessor.getMessage(ex.getDetailKey(), ex.getParams());
		return new ErrorResponse(BAD_REQUEST, type, message);
	}

	@ResponseStatus(BAD_REQUEST)
	@ExceptionHandler(RuntimeException.class)
	public ErrorResponse handleRuntimeException(Exception ex) {
		log.info(ex.getMessage(), ex);
		String type = ex.getClass().getSimpleName();
		String message = ex.getMessage();
		return new ErrorResponse(BAD_REQUEST, type, message);
	}

	@ResponseStatus(NOT_FOUND)
	@ExceptionHandler(NoHandlerFoundException.class)
	public ErrorResponse handleNoHandlerFoundException(Exception ex) {
		String type = ex.getClass().getSimpleName();
		String message = ex.getMessage();
		return new ErrorResponse(NOT_FOUND, type, message);
	}

	@ResponseStatus(INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ErrorResponse handleException(Exception ex) {
		log.error(ex.getMessage(), ex);
		String type = ex.getClass().getSimpleName();
		String message = ex.getMessage();
		return new ErrorResponse(INTERNAL_SERVER_ERROR, type, message);
	}
}
