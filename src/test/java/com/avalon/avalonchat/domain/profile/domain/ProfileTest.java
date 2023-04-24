package com.avalon.avalonchat.domain.profile.domain;

import static com.avalon.avalonchat.testsupport.Fixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.avalon.avalonchat.domain.user.domain.User;

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
		String phoneNumber = "01055110625";

		// when
		// TODO - 처음 생성자에서 profileImageUrl 과 backgroundImageUrl 을 받아도 되지 않을까?
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
