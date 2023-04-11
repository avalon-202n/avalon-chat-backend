package com.avalon.avalonchat.domain.login.exception;

import com.avalon.avalonchat.global.error.exception.AvalonChatRuntimeException;

public class LoginInvalidInputException extends AvalonChatRuntimeException {
	public LoginInvalidInputException(String message) {
		super(message);
	}
}
