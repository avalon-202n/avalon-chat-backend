package com.avalon.avalonchat.domain.profile.repository;

import static com.avalon.avalonchat.testsupport.Fixture.*;
import static java.time.LocalDate.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
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
import com.avalon.avalonchat.domain.profile.dto.ProfileListGetResponse;
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
	private ProfileRepository sut;
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
		LocalDate birthDate = now();
		String nickname = "nickname";
		String phoneNumber = "01055110625";

		Profile profile = new Profile(user, bio, birthDate, nickname, phoneNumber);

		//when & then
		profile.addProfileImage("storage/url/profile_image.png");
		profile.addBackgroundImage("storage/url/background_image.png");
		ProfileImage profileImage = profile.getProfileImages().get(0);
		BackgroundImage backgroundImage = profile.getBackgroundImages().get(0);

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
		User user1 = userRepository.save(createUser("email@gmail1.com", "password1"));
		User user2 = userRepository.save(createUser("email@gmail2.com", "password2"));

		Profile profile1 = sut.save(new Profile(user1, "bio1", now(), "nickname1", "010-1234-5678"));
		Profile profile2 = sut.save(new Profile(user2, "bio2", now(), "nickname2", "010-8765-4321"));

		// when
		List<Profile> foundProfiles = sut.findAllByPhoneNumberIn(List.of("010-1234-5678", "010-8765-4321"));

		// then
		assertThat(foundProfiles).containsExactlyInAnyOrder(profile1, profile2);
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
			myUser, "I'm myUser", of(1997, 8, 21), ",my", "01012345678"
		);
		Profile friendProfile1 = createProfile(
			friendUser1, "I'm friend1", of(1998, 9, 22), "A_friend", "01012123434"
		);
		Profile friendProfile2 = createProfile(
			friendUser2, "I'm friend2", of(1999, 10, 23), "B_friend", "01011112222"
		);
		friendProfile1.addProfileImage("url1");
		friendProfile1.addProfileImage("url2");
		friendProfile2.addProfileImage("url3");
		friendProfile2.addProfileImage("url4");
		Profile savedMyProfile = sut.save(myProfile);
		sut.save(friendProfile1);
		sut.save(friendProfile2);

		// given - ready for friends
		Friend friend1 = new Friend(myProfile, friendProfile1);
		Friend friend2 = new Friend(myProfile, friendProfile2);
		friendRepository.save(friend1);
		friendRepository.save(friend2);

		// when
		List<ProfileListGetResponse> friendProfiles = sut.findAllByMyProfileId(savedMyProfile.getId());

		// then
		assertThat(friendProfiles.size()).isEqualTo(2);
		assertThat(friendProfiles.get(0).getProfileImageUrl()).isEqualTo("url2");
		assertThat(friendProfiles.get(1).getProfileImageUrl()).isEqualTo("url4");
	}
}
