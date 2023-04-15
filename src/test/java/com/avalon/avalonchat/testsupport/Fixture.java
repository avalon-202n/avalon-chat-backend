package com.avalon.avalonchat.testsupport;

import java.time.LocalDate;

import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.Password;
import com.avalon.avalonchat.domain.user.domain.User;

public final class Fixture {

	public static User createUser() {
		return createUser("avalon@e.com", "passw0rd");
	}

	public static User createUser(final String email, final String password) {
		return new User(
			Email.of(email),
			Password.of(password)
		);
	}

	public static Profile createProfile(User user, String bio, LocalDate birthDate, String nickname,
		String phoneNumber) {
		return new Profile(
			user,
			bio,
			birthDate,
			nickname,
			phoneNumber
		);
	}
}
