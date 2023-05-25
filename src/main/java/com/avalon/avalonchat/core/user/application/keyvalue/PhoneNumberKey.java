package com.avalon.avalonchat.core.user.application.keyvalue;

import static lombok.AccessLevel.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor(access = PRIVATE)
@Slf4j
public class PhoneNumberKey {

	private static final String PREFIX = "PHONENUMBER::";

	private final String key;

	public static PhoneNumberKey fromString(String key) {
		return new PhoneNumberKey(key);
	}

	public static PhoneNumberKey ofPurpose(Purpose purpose, String phoneNumber) {
		log.debug("PhoneNumberKey : {}", PREFIX + phoneNumber + purpose.getName());
		return new PhoneNumberKey(PREFIX + phoneNumber + purpose.getName());
	}

	@Override
	public String toString() {
		return PREFIX + key;
	}

	@Getter
	@RequiredArgsConstructor
	public enum Purpose {
		EMAIL_FIND("::EMAIL_FIND"),
		SING_UP("::SING_UP");

		private final String name;
	}
}
