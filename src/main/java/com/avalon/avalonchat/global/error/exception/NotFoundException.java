package com.avalon.avalonchat.global.error.exception;

public class NotFoundException extends BadRequestException {

	static final String MESSAGE_KEY = "not-found";

	public NotFoundException(String targetName, Object key) {
		super(MESSAGE_KEY, targetName, key);
	}
}
