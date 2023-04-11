package com.avalon.avalonchat.domain.profile.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.avalon.avalonchat.domain.profile.domain.BackgroundImage;
import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.Password;
import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.repository.UserRepository;

@DataJpaTest
class BackgroundImageRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProfileRepository profileRepository;
	@Autowired
	private BackgroundImageRepository backgroundImageRepository;

	@DisplayName("BackgroundImage 저장 성공")
	@Test
	void save() {
		//given
		User user = new User(Email.of("email@gmail.com"), Password.of("password"));
		userRepository.save(user);
		Profile profile = new Profile(user, "bio", LocalDate.now(), "nickname");
		profileRepository.save(profile);
		BackgroundImage backgroundImage = new BackgroundImage(profile, "url");

		//when
		backgroundImageRepository.save(backgroundImage);

		//then
		BackgroundImage founBackgroundImage = backgroundImageRepository.findAll().get(0);
		assertThat(founBackgroundImage.getId()).isNotNull();
		assertThat(founBackgroundImage.getUrl()).isEqualTo(backgroundImage.getUrl());
	}
}
