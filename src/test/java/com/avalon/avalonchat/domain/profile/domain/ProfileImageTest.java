package com.avalon.avalonchat.domain.profile.domain;

import com.avalon.avalonchat.testsupport.Fixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ProfileImageTest {

	@DisplayName("profileImage 생성 성공")
	@Test
	void ProfileImage() {
		//given
		Profile profile = Fixture.createProfile();
		String url = "storage/url/image.png";

		//when
		ProfileImage profileImage = new ProfileImage(profile, url);

		//then
		assertThat(profileImage).isNotNull();
	}
}
