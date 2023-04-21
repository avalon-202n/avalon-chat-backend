package com.avalon.avalonchat.global.error.exception;

public final class InputValidationException extends BadRequestException {

	private static final String MESSAGE_KEY = "input-validation";

	public InputValidationException(String detailKey, Object... params) {
		super(MESSAGE_KEY + "." + detailKey, params);
	}
}
