package com.avalon.avalonchat.global.configuration.jwt.exception;

import com.avalon.avalonchat.global.error.exception.AvalonChatRuntimeException;

public class JwtInvalidException extends AvalonChatRuntimeException {
	public JwtInvalidException(String message) {
		super(message);
	}
}
