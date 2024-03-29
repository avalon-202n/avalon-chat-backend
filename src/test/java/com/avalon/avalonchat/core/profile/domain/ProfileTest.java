package com.avalon.avalonchat.core.profile.domain;

import static com.avalon.avalonchat.testsupport.Fixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.avalon.avalonchat.core.user.domain.User;

class ProfileTest {
	@Test
	void profile_생성성공() {
		//given
		User user = createUser("email@gmail.com", "password");
		String bio = "This is my bio";
		LocalDate birthDate = LocalDate.now();
		String nickname = "nickname";
		String profileUrl = "storage/url/profile_image.png";
		String backgroundUrl = "storage/url/background_image.png";
		PhoneNumber phoneNumber = PhoneNumber.of("010-5511-0625");

		// when
		Profile profile = new Profile(user, bio, birthDate, nickname, phoneNumber);
		profile.addProfileImage("storage/url/profile_image.png");
		profile.addBackgroundImage("storage/url/background_image.png");

		//then
		assertThat(profile.getUser()).isEqualTo(user);
		assertThat(profile.getBio()).isEqualTo(bio);
		assertThat(profile.getBirthDate()).isEqualTo(birthDate);
		assertThat(profile.getNickname()).isEqualTo(nickname);
		assertThat(profile.getProfileImages().get(0).getUrl()).isEqualTo(profileUrl);
		assertThat(profile.getBackgroundImages().get(0).getUrl()).isEqualTo(backgroundUrl);
	}

}
