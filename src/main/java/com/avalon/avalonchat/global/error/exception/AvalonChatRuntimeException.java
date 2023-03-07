package com.avalon.avalonchat.global.error.exception;

/**
 * Root RuntimeException for this Application
 */
public class AvalonChatRuntimeException extends RuntimeException {

	public AvalonChatRuntimeException() {
	}

	public AvalonChatRuntimeException(String message) {
		super(message);
	}

	public AvalonChatRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public AvalonChatRuntimeException(Throwable cause) {
		super(cause);
	}

	public AvalonChatRuntimeException(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
