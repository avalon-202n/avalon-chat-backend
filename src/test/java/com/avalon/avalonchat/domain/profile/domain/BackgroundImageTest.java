package com.avalon.avalonchat.domain.profile.domain;

import com.avalon.avalonchat.testsupport.Fixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BackgroundImageTest {

	@DisplayName("profileImage 생성 성공")
	@Test
	void backgroundImage() {
		//given
		Profile profile = Fixture.createProfile();
		String url = "storage/url/image.png";

		//when
		BackgroundImage backgroundImage = new BackgroundImage(profile, url);

		//then
		assertThat(backgroundImage).isNotNull();
	}
}
