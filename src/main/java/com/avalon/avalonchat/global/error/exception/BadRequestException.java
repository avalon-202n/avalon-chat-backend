package com.avalon.avalonchat.global.error.exception;

public class BadRequestException extends AvalonChatRuntimeException {

	protected static final String MESSAGE_KEY = "error.bad-request";

	public BadRequestException(String detailKey, Object... params) {
		super(MESSAGE_KEY + "." + detailKey, params);
	}
}
