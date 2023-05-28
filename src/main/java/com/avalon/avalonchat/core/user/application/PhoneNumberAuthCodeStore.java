package com.avalon.avalonchat.core.user.application;

import com.avalon.avalonchat.core.user.application.keyvalue.AuthCodeValue;
import com.avalon.avalonchat.core.user.application.keyvalue.PhoneNumberKey;

public interface PhoneNumberAuthCodeStore {

	void save(PhoneNumberKey key, AuthCodeValue value);

	boolean isAuthenticated(PhoneNumberKey key);

	boolean checkKeyValueMatches(PhoneNumberKey key, AuthCodeValue value);
}
