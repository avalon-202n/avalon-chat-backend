package com.avalon.avalonchat.global.error.exception;

/**
 * Root RuntimeException for this Application
 */
public abstract class AvalonChatRuntimeException extends RuntimeException {

	private final String messageKey;

	private final String detailKey;

	private final Object[] params;

	public AvalonChatRuntimeException(String messageKey, String detailKey, Object[] params) {
		this.messageKey = messageKey;
		this.detailKey = detailKey;
		this.params = params;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public String getDetailKey() {
		return detailKey;
	}

	public Object[] getParams() {
		return params;
	}
}
