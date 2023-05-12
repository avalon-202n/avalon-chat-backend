package com.avalon.avalonchat.domain.user.keyvalue;

import static lombok.AccessLevel.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = PRIVATE)
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
