package com.avalon.avalonchat.core.user.application;

import com.avalon.avalonchat.core.user.application.keyvalue.AuthCodeValue;
import com.avalon.avalonchat.core.user.application.keyvalue.PhoneNumberKey;
import com.avalon.avalonchat.global.keyvalue.KeyValueStore;

public interface PhoneNumberAuthCodeStore extends KeyValueStore<PhoneNumberKey, AuthCodeValue> {

	default boolean isAuthenticated(PhoneNumberKey key) {
		AuthCodeValue authCodeValue = get(key);
		return authCodeValue != null && authCodeValue.isAuthenticated();
	}

	default boolean checkKeyValueMatches(PhoneNumberKey key, String authCodeValue) {
		AuthCodeValue gotAuthCodeValue = get(key);
		boolean checked = gotAuthCodeValue != null && gotAuthCodeValue.matches(authCodeValue);
		if (checked) {
			// do authenticate
			put(key, AuthCodeValue.ofAuthenticated());
		}
		return checked;
	}
}
