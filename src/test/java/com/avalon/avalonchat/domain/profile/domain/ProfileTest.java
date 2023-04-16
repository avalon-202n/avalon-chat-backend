package com.avalon.avalonchat.domain.profile.domain;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.Password;
import com.avalon.avalonchat.domain.user.domain.User;

class ProfileTest {
	@Test
	void profile_생성성공() {
		//given
		User user = new User(Email.of("email@gmail.com"), Password.of("password"));

		String bio = "This is my bio";
		LocalDate birthDate = LocalDate.now();
		String nickname = "nickname";

		String profileUrl = "storage/url/profile_image.png";
		String backgroundUrl = "storage/url/background_image.png";

		String phoneNumber = "01055110625";

		// when
		Profile profile = new Profile(user, bio, birthDate, nickname, phoneNumber);

		ProfileImage profileImage = new ProfileImage(profile, profileUrl);
		BackgroundImage backgroundImage = new BackgroundImage(profile, backgroundUrl);

		profile.addProfileImage(profileImage);
		profile.addBackgroundImage(backgroundImage);

		//then
		assertThat(profile.getUser()).isEqualTo(user);
		assertThat(profile.getBio()).isEqualTo(bio);
		assertThat(profile.getBirthDate()).isEqualTo(birthDate);
		assertThat(profile.getNickname()).isEqualTo(nickname);
		assertThat(profile.getProfileImages().get(0).getUrl()).isEqualTo(profileUrl);
		assertThat(profile.getBackgroundImages().get(0).getUrl()).isEqualTo(backgroundUrl);
	}

}
