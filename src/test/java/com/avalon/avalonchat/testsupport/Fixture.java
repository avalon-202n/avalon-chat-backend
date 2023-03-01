package com.avalon.avalonchat.testsupport;

import com.avalon.avalonchat.domain.user.domain.User;

public final class Fixture {

	public static User createUser() {
		return new User(
			"avalon@e.com",
			"passW0rd",
			"happy"
		);
	}
}
