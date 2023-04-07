package com.avalon.avalonchat.domain.profile.domain;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.User;

class ProfileImageTest {
	@Test
	void profileImage_생성성공() {
		//given
		User user = new User(Email.of("email@gmail.com"), "password");
		Profile profile = new Profile(user, "bio", LocalDate.now(), "nickname");
		String url = "storage/url/image.png";

		//when
		ProfileImage profileImage = new ProfileImage(profile, url);

		//then
		assertThat(profileImage).isNotNull();
	}
}
