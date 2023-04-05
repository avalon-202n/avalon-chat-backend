package com.avalon.avalonchat.domain.login.exception;

import com.avalon.avalonchat.global.error.exception.AvalonChatRuntimeException;

public class LoginInvalidInputException extends AvalonChatRuntimeException {
	public LoginInvalidInputException(String mesage) {
		super(mesage);
	}
}
