package com.avalon.avalonchat.domain.profile.service;

import static com.avalon.avalonchat.testsupport.Fixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.dto.ProfileAddRequest;
import com.avalon.avalonchat.domain.profile.repository.ProfileRepository;
import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.repository.UserRepository;

@SpringBootTest
class ProfileServiceImplTest {

	@Autowired
	ProfileServiceImpl sut;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ProfileRepository profileRepository;

	@Test
	void userId_로_profileId_조회_성공() {
		//given
		User user = createUser("hello@world.com", "password");
		User savedUser = userRepository.save(user);

		Profile profile = createProfile(user, "hi there", LocalDate.of(1997, 8, 21), "haha");
		Profile savedProfile = profileRepository.save(profile);

		//when
		long foundProfileId = sut.getProfileIdByUserId(savedUser.getId());

		//then
		assertThat(foundProfileId).isEqualTo(savedProfile.getId());
	}

	private ProfileAddRequest profileAddRequest() {
		return new ProfileAddRequest(
			LocalDate.of(1997, 8, 21),
			"haha",
			"hi there",
			null,
			null
		);
	}
}
