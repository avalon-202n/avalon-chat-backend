package com.avalon.avalonchat.core.profile.domain;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.avalon.avalonchat.core.user.domain.Email;
import com.avalon.avalonchat.core.user.domain.Password;
import com.avalon.avalonchat.core.user.domain.User;

class BackgroundImageTest {

	@Test
	void backgroundImage_생성성공() {
		//given
		User user = new User(Email.of("email@gmail.com"), Password.of("password"));
		Profile profile = new Profile(user, "bio", LocalDate.now(), "nickname", PhoneNumber.of("010-5511-0625"));
		String url = "storage/url/image.png";

		//when
		BackgroundImage backgroundImage = new BackgroundImage(profile, url);

		//then
		assertThat(backgroundImage).isNotNull();
	}
}
