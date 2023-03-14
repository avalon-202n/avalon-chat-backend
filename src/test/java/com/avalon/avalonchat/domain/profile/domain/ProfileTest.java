package com.avalon.avalonchat.domain.profile.domain;

import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.testsupport.Fixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ProfileTest {

	@DisplayName("profile 생성 성공")
	@Test
	void profile() {
		//given
		User user = Fixture.createUser();
		String bio = "This is my bio";
		LocalDate birthDate = LocalDate.now();
		String nickname = "nickname";

		// when
		Profile profile = new Profile(
			user, bio, birthDate, nickname
		);

		//then
		assertThat(profile).isNotNull();
	}

}
