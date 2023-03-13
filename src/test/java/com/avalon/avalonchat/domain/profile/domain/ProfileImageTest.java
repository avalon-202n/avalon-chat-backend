package com.avalon.avalonchat.domain.profile.domain;

import com.avalon.avalonchat.testsupport.Fixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ProfileImageTest {

	@DisplayName("profileImage 생성 성공")
	@Test
	void profile() {
		//given
		Profile profile = Fixture.createProfile();
		String filePath = "src/test/resources/store/images/test.png";

		// when
		ProfileImage profileImage = new ProfileImage(profile, filePath);

		//then
		assertThat(profileImage).isNotNull();
	}

}
