package com.avalon.avalonchat.global.error;

import static org.apache.commons.lang3.ObjectUtils.*;

import org.springframework.http.HttpStatus;

import lombok.Data;

/**
 * 예외에 대한 응답 포맷
 */
@Data
public class ErrorResponse {

	private final int code;
	private final String type;
	private final String message;

	public ErrorResponse(HttpStatus httpStatus, Exception exception) {
		this(
			httpStatus.value(),
			exception.getClass().getSimpleName(),
			defaultIfNull(exception.getMessage(), "")
		);
	}

	public ErrorResponse(int code, String type, String message) {
		this.code = code;
		this.type = type;
		this.message = message;
	}
}
