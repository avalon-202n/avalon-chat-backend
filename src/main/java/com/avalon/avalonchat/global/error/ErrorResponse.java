package com.avalon.avalonchat.global.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * 예외에 대한 응답 포맷
 */
@Getter
public class ErrorResponse {

	private final int code;
	private final String type;
	private final String message;

	public ErrorResponse(HttpStatus httpStatus, Exception exception) {
		this.code = httpStatus.value();
		this.type = exception.getClass().getSimpleName();
		this.message = exception.getMessage();
	}
}
