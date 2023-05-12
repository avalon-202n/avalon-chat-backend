package com.avalon.avalonchat.domain.user.keyvalue;

import static com.avalon.avalonchat.global.util.Preconditions.*;

import com.avalon.avalonchat.domain.user.domain.Email;

public class EmailKey {

	private static final String PREFIX = "EMAIL::";

	private final Email email;

	private EmailKey(Email email) {
		checkNotNull(email, "EmailKey.email cannot be null");

		this.email = email;
	}

	public static EmailKey fromString(String string) {
		return new EmailKey(Email.of(string));
	}

	@Override
	public String toString() {
		return PREFIX + email;
	}
}
