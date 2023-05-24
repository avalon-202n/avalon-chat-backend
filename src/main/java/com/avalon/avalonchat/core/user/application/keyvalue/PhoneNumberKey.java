package com.avalon.avalonchat.core.user.application.keyvalue;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PhoneNumberKey {

	private static final String PREFIX = "PHONENUMBER::";

	private final String key;

	public static PhoneNumberKey fromString(String key) {
		return new PhoneNumberKey(key);
	}

	@Override
	public String toString() {
		return PREFIX + key;
	}
}
