package com.avalon.avalonchat.domain.profile.repository;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.repository.UserRepository;
import com.avalon.avalonchat.testsupport.Fixture;

@DataJpaTest
class ProfileRepositoryTest {

	@Autowired
	private ProfileRepository profileRepository;
	@Autowired
	private UserRepository userRepository;

	@DisplayName("profile 저장 성공")
	@Test
	void save() {
		//given
		User user = Fixture.createUser();
		userRepository.save(user);

		String bio = "bio";
		LocalDate birthDate = LocalDate.now();
		String nickname = "nickname";

		Profile profile = new Profile(user, bio, birthDate, nickname);

		//when
		profileRepository.save(profile);

		//then
		Profile foundProfile = profileRepository.findAll().get(0);
		assertThat(foundProfile.getId()).isNotNull();
		assertThat(foundProfile.getBio()).isEqualTo(profile.getBio());
		assertThat(foundProfile.getBirthDate()).isEqualTo(profile.getBirthDate());
		assertThat(foundProfile.getNickname()).isEqualTo(profile.getNickname());
		assertThat(foundProfile.getCreatedAt()).isNotNull();
		assertThat(foundProfile.getUpdatedAt()).isNotNull();
	}

	@Test
	@Transactional
	void findAllByPhoneNumber() {
		// given
		String phoneNumber1 = "010-1234-5678";
		String phoneNumber2 = "010-8765-4321";
		String[] phoneNumbers = {phoneNumber1, phoneNumber2};

		User myUser = Fixture.createUser("myUser1@gmail.com", "myUser1");
		User friendUser1 = Fixture.createUser("friendUser1@gmail.com", "friendUser1");
		User friendUser2 = Fixture.createUser("friendUser2@gmail.com", "friendUser2");
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
		List<Profile> savedProfiles = profileRepository.findAllByPhoneNumber(phoneNumbers);

		// then
		assertThat(savedProfiles.containsAll(profiles)).isTrue();
	}
}
