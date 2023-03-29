package com.avalon.avalonchat.domain.profile.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.domain.ProfileImage;
import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.repository.UserRepository;

@DataJpaTest
class ProfileImageRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProfileRepository profileRepository;
	@Autowired
	private ProfileImageRepository profileImageRepository;

	@DisplayName("ProfileImage 저장 성공")
	@Test
	void save() {
		//given
		User user = new User(Email.of("email@gmail.com"), "password");
		userRepository.save(user);
		Profile profile = new Profile(user, "bio", LocalDate.now(), "nickname");
		profileRepository.save(profile);
		ProfileImage profileImage = new ProfileImage(profile, "url");

		//when
		profileImageRepository.save(profileImage);

		//then
		ProfileImage foundProfileImage = profileImageRepository.findAll().get(0);
		assertThat(foundProfileImage.getId()).isNotNull();
		assertThat(foundProfileImage.getUrl()).isEqualTo(profileImage.getUrl());
	}
}
