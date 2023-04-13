package com.avalon.avalonchat.domain.profile.service;

import static com.avalon.avalonchat.testsupport.Fixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.dto.ProfileAddRequest;
import com.avalon.avalonchat.domain.profile.dto.ProfileAddResponse;
import com.avalon.avalonchat.domain.profile.repository.ProfileRepository;
import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.Password;
import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.repository.UserRepository;

@SpringBootTest
class ProfileServiceImplTest {

	@Autowired
	private ProfileServiceImpl sut;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProfileRepository profileRepository;

	@Test
	void addProfile_성공() {
		// given
		User user = new User(Email.of("email@gmail.com"), Password.of("password"));
		User savedUser = userRepository.save(user);

		LocalDate birthDate = LocalDate.now();
		String nickname = "nickname";
		String bio = "bio";
		String profileImageUrl = "profileImageUrl";
		String backgroundImageUrl = "backgroundImageUrl";
		ProfileAddRequest request = new ProfileAddRequest(birthDate, nickname, bio, profileImageUrl,
			backgroundImageUrl);

		// when
		ProfileAddResponse response = sut.addProfile(savedUser.getId(), request);

		// then
		assertThat(response.getBirthDate()).isEqualTo(birthDate);
		assertThat(response.getNickname()).isEqualTo(nickname);
		assertThat(response.getBio()).isEqualTo(bio);
		assertThat(response.getProfileImages()[0]).isEqualTo(profileImageUrl);
		assertThat(response.getBackgroundImages()[0]).isEqualTo(backgroundImageUrl);
	}

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
}

