package com.avalon.avalonchat.global.error;

import static org.apache.commons.lang3.ObjectUtils.*;

import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 예외에 대한 응답 포맷
 */
@Data
public class ErrorResponse {

	@Schema(description = "예외의 HTTP 응답 코드")
	@NotNull
	private final int code;

	@Schema(description = "예외의 타입")
	@NotNull
	private final String type;

	@Schema(description = "예외 메시지")
	@NotNull
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
