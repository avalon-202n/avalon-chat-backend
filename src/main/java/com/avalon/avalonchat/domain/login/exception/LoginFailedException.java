package com.avalon.avalonchat.domain.login.exception;

import com.avalon.avalonchat.global.error.exception.AvalonChatRuntimeException;

public class LoginFailedException extends AvalonChatRuntimeException {

	static final String MESSAGE_KEY = "error.bad-request.login-failed";

	public LoginFailedException(String detailKey, Object... params) {
		super(MESSAGE_KEY, MESSAGE_KEY + "." + detailKey, params);
	}
}
