package com.avalon.avalonchat.domain.profile.exception;

import com.avalon.avalonchat.global.error.exception.AvalonChatRuntimeException;

public class UnAuthenticatedPhoneNumberException extends AvalonChatRuntimeException {
	public UnAuthenticatedPhoneNumberException(String message) {
		super(message);
	}
}
