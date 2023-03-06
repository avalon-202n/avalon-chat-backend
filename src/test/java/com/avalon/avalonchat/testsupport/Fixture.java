package com.avalon.avalonchat.testsupport;

import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.User;

public final class Fixture {

	public static User createUser() {
		return createUser("avalon@e.com", "passw0rd");
	}

	public static User createUser(final String email, final String password) {
		return new User(
			Email.of(email),
			password
		);
	}

}
