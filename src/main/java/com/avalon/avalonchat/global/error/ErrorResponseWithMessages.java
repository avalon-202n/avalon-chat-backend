package com.avalon.avalonchat.global.error;

import java.text.MessageFormat;

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

	BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

	SIGNUP_BAD_REQUEST(HttpStatus.BAD_REQUEST, "이메일 혹은 비밀번호의 형식이 올바르지 않습니다."),
	INVALID_FIELD(HttpStatus.BAD_REQUEST, "{0}의 형식이 올바르지 않습니다."),
	INVALID_LENGTH(HttpStatus.BAD_REQUEST, "{0}의 길이는 {1}이상 {2}이하 여야 합니다");

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

	public ErrorResponse toInstance(Object[] args) {
		String formattedMessage = MessageFormat.format(message, args);
		return new ErrorResponse(status.value(), type, formattedMessage);
	}

	public String getMessage(Object[] args) {
		return MessageFormat.format(message, args);
	}
}
