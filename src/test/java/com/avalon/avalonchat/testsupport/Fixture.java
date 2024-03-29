package com.avalon.avalonchat.testsupport;

import java.time.LocalDate;

import com.avalon.avalonchat.core.friend.domain.Friend;
import com.avalon.avalonchat.core.profile.domain.PhoneNumber;
import com.avalon.avalonchat.core.profile.domain.Profile;
import com.avalon.avalonchat.core.user.domain.Email;
import com.avalon.avalonchat.core.user.domain.Password;
import com.avalon.avalonchat.core.user.domain.User;

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

	public static Profile createProfile(User user) {
		return createProfile(
			user,
			"bio",
			LocalDate.of(2023, 4, 20),
			"nickname",
			"010-1234-5678"
		);
	}

	public static Profile createProfile(
		User user,
		String bio,
		LocalDate birthDate,
		String nickname,
		String phoneNumber
	) {
		return new Profile(
			user,
			bio,
			birthDate,
			nickname,
			PhoneNumber.of(phoneNumber)
		);
	}

	public static Friend createFriend(Profile myProfile, Profile friendProfile, String friendName) {
		return new Friend(myProfile, friendProfile, friendName);
	}

	// public static Friend createFriend(Profile myProfile, Profile friendProfile, String friendName) {
	// 	return new Friend(myProfile, friendProfile);
	// }
}
