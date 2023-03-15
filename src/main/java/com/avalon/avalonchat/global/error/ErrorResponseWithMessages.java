package com.avalon.avalonchat.global.error;

import org.springframework.http.HttpStatus;

import com.avalon.avalonchat.global.error.exception.AvalonChatRuntimeException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 예외 Http 응답 코드와 메시지를 저장하는 enum
 * - TODO - MessageSource 를 사용해도 좋을 듯
 */
@Getter
@RequiredArgsConstructor
public enum ErrorResponseWithMessages {

	SIGNUP_BAD_REQUEST(HttpStatus.BAD_REQUEST, "이메일 혹은 비밀번호의 형식이 올바르지 않습니다.");

	private final HttpStatus status;
	private final String type;
	private final String message;

	ErrorResponseWithMessages(HttpStatus status, String message) {
		this(status, message, AvalonChatRuntimeException.class);
	}

	ErrorResponseWithMessages(HttpStatus status, String message, Class<?> clazz) {
		this.status = status;
		this.message = message;
		this.type = clazz.getSimpleName();
	}

	public ErrorResponse toInstance() {
		return new ErrorResponse(status.value(), type, message);
	}
}
