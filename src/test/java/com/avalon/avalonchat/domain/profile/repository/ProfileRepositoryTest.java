package com.avalon.avalonchat.domain.profile.repository;

import static com.avalon.avalonchat.testsupport.Fixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.domain.friend.domain.Friend;
import com.avalon.avalonchat.domain.friend.repository.FriendRepository;
import com.avalon.avalonchat.domain.profile.domain.BackgroundImage;
import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.domain.ProfileImage;
import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.Password;
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
	@Autowired
	private FriendRepository friendRepository;

	@Test
	void profile_영속성_저장성공() {
		//given
		User user = new User(Email.of("email@gmail.com"), Password.of("password"));
		userRepository.save(user);

		String bio = "bio";
		LocalDate birthDate = LocalDate.now();
		String nickname = "nickname";

		String profileUrl = "storage/url/profile_image.png";
		String backgroundUrl = "storage/url/background_image.png";

		String phoneNumber = "01055110625";

		Profile profile = new Profile(user, bio, birthDate, nickname, phoneNumber);

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
		List<String> phoneNumbers = new ArrayList<>();
		phoneNumbers.add(phoneNumber1);
		phoneNumbers.add(phoneNumber2);
		String phoneNumber = "01055110625";

		User myUser = new User(Email.of("email@gmail.com"), Password.of("password"));
		User friendUser1 = new User(Email.of("email@gmail1.com"), Password.of("password1"));
		User friendUser2 = new User(Email.of("email@gmail2.com"), Password.of("password2"));
		User savedMyUser = userRepository.save(myUser);
		User savedFriendUser1 = userRepository.save(friendUser1);
		User savedFriendUser2 = userRepository.save(friendUser2);

		Profile myProfile = new Profile(savedMyUser, "bio", LocalDate.now(), "nickname", phoneNumber);
		Profile friendProfile1 = new Profile(savedFriendUser1, "bio1", LocalDate.now(), "nickname1", phoneNumber);
		Profile friendProfile2 = new Profile(savedFriendUser2, "bio2", LocalDate.now(), "nickname2", phoneNumber);
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

	@Test
	void myProfileId로_friendProfiles_조회성공() {
		//given - ready for users
		User myUser = createUser("myUser@world.com", "myUserPassword");
		User friendUser1 = createUser("friendUser1@world.com", "friendUser1");
		User friendUser2 = createUser("friendUser2@world.com", "friendUser2");
		userRepository.save(myUser);
		userRepository.save(friendUser1);
		userRepository.save(friendUser2);

		// given - ready for profiles
		Profile myProfile = createProfile(
			myUser, "I'm myUser", LocalDate.of(1997, 8, 21), ",my", "01012345678"
		);
		Profile friendProfile1 = createProfile(
			friendUser1, "I'm friend1", LocalDate.of(1998, 9, 22), ",friend", "01012123434"
		);
		Profile friendProfile2 = createProfile(
			friendUser2, "I'm friend2", LocalDate.of(1999, 10, 23), ",my", "01011112222"
		);
		ProfileImage profileImage1 = new ProfileImage(friendProfile1, "url1");
		ProfileImage profileImage2 = new ProfileImage(friendProfile1, "url2");
		ProfileImage profileImage3 = new ProfileImage(friendProfile2, "url3");
		ProfileImage profileImage4 = new ProfileImage(friendProfile2, "url4");
		friendProfile1.addProfileImage(profileImage1);
		friendProfile1.addProfileImage(profileImage2);
		friendProfile2.addProfileImage(profileImage3);
		friendProfile2.addProfileImage(profileImage4);
		Profile savedMyProfile = profileRepository.save(myProfile);
		Profile savedFriendProfile1 = profileRepository.save(friendProfile1);
		Profile savedFriendProfile2 = profileRepository.save(friendProfile2);

		// given - ready for friends
		Friend friend1 = new Friend(myProfile, friendProfile1);
		Friend friend2 = new Friend(myProfile, friendProfile2);
		friendRepository.save(friend1);
		friendRepository.save(friend2);

		// when & then
		List<Profile> friendProfiles = profileRepository.findAllByMyProfileId(savedMyProfile.getId());

		assertThat(friendProfiles.size()).isEqualTo(2);
		for (Profile profile : friendProfiles) {
			assertThat(profile.getId()).isIn(savedFriendProfile1.getId(), savedFriendProfile2.getId());
		}

		profileRepository.findAllByMyProfileId(savedMyProfile.getId()).stream()
			.forEach(profile -> assertThat(profile.getProfileImages().size()).isEqualTo(2));
	}
}
