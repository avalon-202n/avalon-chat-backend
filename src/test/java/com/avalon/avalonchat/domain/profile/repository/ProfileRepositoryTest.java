package com.avalon.avalonchat.domain.profile.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.domain.profile.domain.BackgroundImage;
import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.domain.ProfileImage;
import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.repository.UserRepository;

@DataJpaTest
@Transactional
class ProfileRepositoryTest {
	@Autowired
	private EntityManager em;
	@Autowired
	private ProfileRepository profileRepository;
	@Autowired
	private UserRepository userRepository;

	@Test
	void profile_영속성_저장성공() {
		//given
		User user = new User(Email.of("email@gmail.com"), "password");
		userRepository.save(user);

		String bio = "bio";
		LocalDate birthDate = LocalDate.now();
		String nickname = "nickname";

		String profileUrl = "storage/url/profile_image.png";
		String backgroundUrl = "storage/url/background_image.png";

		Profile profile = new Profile(user, bio, birthDate, nickname);

		ProfileImage profileImage = new ProfileImage(profile, profileUrl);
		BackgroundImage backgroundImage = new BackgroundImage(profile, backgroundUrl);

		//when & then
		profile.addProfileImage(profileImage);
		profile.addBackgroundImage(backgroundImage);

		em.persist(profile);

		assertThat(em.contains(profile)).isTrue();
		assertThat(em.contains(profileImage)).isTrue();
		assertThat(em.contains(backgroundImage)).isTrue();

		em.flush();
		em.clear();
		ProfileImage savedProfileImage = em.find(ProfileImage.class, profileImage.getId());
		BackgroundImage savedBackgroundImage = em.find(BackgroundImage.class, backgroundImage.getId());

		assertThat(savedProfileImage.getUrl()).isEqualTo(profile.getProfileImages().get(0).getUrl());
		assertThat(savedBackgroundImage.getUrl()).isEqualTo(profile.getBackgroundImages().get(0).getUrl());
	}

	@Test
	void findAllByPhoneNumber_조회성공() {
		// given
		String phoneNumber1 = "010-1234-5678";
		String phoneNumber2 = "010-8765-4321";
		String[] phoneNumbers = {phoneNumber1, phoneNumber2};

		User myUser = new User(Email.of("email@gmail.com"), "password");
		User friendUser1 = new User(Email.of("email@gmail1.com"), "password1");
		User friendUser2 = new User(Email.of("email@gmail2.com"), "password2");
		User savedMyUser = userRepository.save(myUser);
		User savedFriendUser1 = userRepository.save(friendUser1);
		User savedFriendUser2 = userRepository.save(friendUser2);

		Profile myProfile = new Profile(savedMyUser, "bio", LocalDate.now(), "nickname");
		Profile friendProfile1 = new Profile(savedFriendUser1, "bio1", LocalDate.now(), "nickname1");
		Profile friendProfile2 = new Profile(savedFriendUser2, "bio2", LocalDate.now(), "nickname2");
		friendProfile1.setPhoneNumber(phoneNumber1);
		friendProfile2.setPhoneNumber(phoneNumber2);
		profileRepository.save(myProfile);
		profileRepository.save(friendProfile1);
		profileRepository.save(friendProfile2);
		List<Profile> profiles = new ArrayList<>();
		profiles.add(friendProfile1);
		profiles.add(friendProfile2);

		// when
		List<Profile> savedProfiles = profileRepository.findAllByPhoneNumberIn(phoneNumbers);

		// then
		assertThat(savedProfiles.containsAll(profiles)).isTrue();
	}
}
