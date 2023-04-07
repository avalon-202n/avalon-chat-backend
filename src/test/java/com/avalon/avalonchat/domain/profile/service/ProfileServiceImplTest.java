package com.avalon.avalonchat.domain.profile.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avalon.avalonchat.domain.profile.dto.ProfileAddRequest;
import com.avalon.avalonchat.domain.profile.dto.ProfileAddResponse;
import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.repository.UserRepository;

@SpringBootTest
class ProfileServiceImplTest {

	@Autowired
	private ProfileServiceImpl profileService;
	@Autowired
	private UserRepository userRepository;

	@Test
	void addProfile_성공() {
		// given
		User user = new User(Email.of("email@gmail.com"), "password");
		User savedUser = userRepository.save(user);

		LocalDate birthDate = LocalDate.now();
		String nickname = "nickname";
		String bio = "bio";
		String profileImageUrl = "profileImageUrl";
		String backgroundImageUrl = "backgroundImageUrl";
		ProfileAddRequest request = new ProfileAddRequest(birthDate, nickname, bio, profileImageUrl,
			backgroundImageUrl);

		// when
		ProfileAddResponse response = profileService.addProfile(savedUser.getId(), request);

		// then
		assertThat(response.getBirthDate()).isEqualTo(birthDate);
		assertThat(response.getNickname()).isEqualTo(nickname);
		assertThat(response.getBio()).isEqualTo(bio);
		assertThat(response.getProfileImages()[0]).isEqualTo(profileImageUrl);
		assertThat(response.getBackgroundImages()[0]).isEqualTo(backgroundImageUrl);
	}
}
