package com.avalon.avalonchat.domain.profile.repository;

import static com.avalon.avalonchat.testsupport.Fixture.*;
import static java.time.LocalDate.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.domain.friend.domain.Friend;
import com.avalon.avalonchat.domain.friend.repository.FriendRepository;
import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.dto.ProfileListGetResponse;
import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.repository.UserRepository;

@Transactional
@DataJpaTest
class ProfileRepositoryTest {
	@Autowired
	private ProfileRepository sut;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FriendRepository friendRepository;

	@Test
	void profile_영속성_저장성공() {
		//given
		User user = createUser();
		Profile profile = createProfile(
			user,
			"bio",
			LocalDate.of(2023, 4, 20),
			"nickname",
			"010-1234-5678"
		);

		profile.addProfileImage("http://profile/image/url");
		profile.addBackgroundImage("http://background/image/url");

		// when
		sut.save(profile);

		// then
		assertThat(profile.getUser()).isEqualTo(user);
		assertThat(profile.getBio()).isEqualTo("bio");
		assertThat(profile.getBirthDate()).isEqualTo(LocalDate.of(2023, 4, 20));
		assertThat(profile.getNickname()).isEqualTo("nickname");
		assertThat(profile.getPhoneNumber()).isEqualTo("010-1234-5678");
		assertThat(profile.getLatestProfileImageUrl()).isEqualTo("http://profile/image/url");

		assertThat(profile.getProfileImages()).hasSize(1);
		assertThat(profile.getBackgroundImages()).hasSize(1);
		assertThat(profile.getProfileImages().get(0).getUrl()).isEqualTo("http://profile/image/url");
		assertThat(profile.getBackgroundImages().get(0).getUrl()).isEqualTo("http://background/image/url");
	}

	@Test
	void findAllByPhoneNumber_조회성공() {
		// given
		User user1 = userRepository.save(createUser("email@gmail1.com", "password1"));
		User user2 = userRepository.save(createUser("email@gmail2.com", "password2"));

		Profile profile1 = sut.save(
			createProfile(user1, "bio1", now(), "nickname1", "010-1234-5678"));
		Profile profile2 = sut.save(
			createProfile(user2, "bio2", now(), "nickname2", "010-1234-1234"));

		// when
		List<Profile> foundProfiles = sut.findAllByPhoneNumberIn(List.of("010-1234-5678", "010-1234-1234"));

		// then
		assertThat(foundProfiles).containsExactlyInAnyOrder(profile1, profile2);
	}

	@Test
	void findAllByMyProfileId_조회성공() {
		//given
		User myUser = createUser("myUser@world.com", "myUserPassword");
		User friendUser1 = createUser("friendUser1@world.com", "friendUser1");
		User friendUser2 = createUser("friendUser2@world.com", "friendUser2");
		userRepository.saveAll(List.of(myUser, friendUser1, friendUser2));

		Profile myProfile = createProfile(
			myUser, "I'm myUser", of(1997, 8, 21),
			",my", "01012345678"
		);
		Profile friendProfile1 = createProfile(
			friendUser1, "I'm friend1", of(1998, 9, 22),
			"A_friend", "01012123434"
		);
		Profile friendProfile2 = createProfile(
			friendUser2, "I'm friend2", of(1999, 10, 23),
			"B_friend", "01011112222"
		);
		friendProfile1.addProfileImage("url1");
		friendProfile1.addProfileImage("url2");
		friendProfile2.addProfileImage("url3");
		friendProfile2.addProfileImage("url4");
		sut.saveAll(List.of(myProfile, friendProfile1, friendProfile2));

		Friend friend1 = createFriend(myProfile, friendProfile1);
		Friend friend2 = createFriend(myProfile, friendProfile2);
		friendRepository.saveAll(List.of(friend1, friend2));

		// when
		List<ProfileListGetResponse> foundFriendProfiles = sut.findAllByMyProfileId(myProfile.getId());

		// then
		assertThat(foundFriendProfiles.get(0).getNickname()).isEqualTo(friendProfile1.getNickname());
		assertThat(foundFriendProfiles.get(0).getBio()).isEqualTo(friendProfile1.getBio());
		assertThat(foundFriendProfiles.get(0).getProfileImageUrl())
			.isEqualTo(friendProfile1.getLatestProfileImageUrl());
		assertThat(foundFriendProfiles.get(1).getNickname()).isEqualTo(friendProfile2.getNickname());
		assertThat(foundFriendProfiles.get(1).getBio()).isEqualTo(friendProfile2.getBio());
		assertThat(foundFriendProfiles.get(1).getProfileImageUrl())
			.isEqualTo(friendProfile2.getLatestProfileImageUrl());
	}

	@Test
	void findLatestProfileImageUrl_조회성공() {
		// given
		User user = createUser();
		Profile profile = createProfile(user);

		profile.addProfileImage("http://profile/image/url1");
		profile.addProfileImage("http://profile/image/url2");
		profile.addProfileImage("http://profile/image/url3");
		profile.addProfileImage("http://profile/image/url4");
		profile.addProfileImage("http://profile/image/url5");

		userRepository.save(user);
		sut.save(profile);

		// when
		String optionalLatestProfileImageUrl = sut.findLatestProfileImageUrl(profile.getId()).get();

		// then
		assertThat(optionalLatestProfileImageUrl).isEqualTo("http://profile/image/url5");
	}

	@Test
	void findAllFriendPhoneNumbersByMyProfileId_조회성공() {
		// given
		User myUser = createUser("myUser@world.com", "myUserPassword");
		User friendUser1 = createUser("friendUser1@world.com", "friendUser1");
		User friendUser2 = createUser("friendUser2@world.com", "friendUser2");
		userRepository.saveAll(List.of(myUser, friendUser1, friendUser2));

		Profile myProfile = createProfile(
			myUser, "I'm myUser", of(1997, 8, 21),
			",my", "01012345678"
		);
		Profile friendProfile1 = createProfile(
			friendUser1, "I'm friend1", of(1998, 9, 22),
			"A_friend", "01012123434"
		);
		Profile friendProfile2 = createProfile(
			friendUser2, "I'm friend2", of(1999, 10, 23),
			"B_friend", "01011112222"
		);
		sut.saveAll(List.of(myProfile, friendProfile1, friendProfile2));

		Friend friend1 = createFriend(myProfile, friendProfile1);
		Friend friend2 = createFriend(myProfile, friendProfile2);
		friendRepository.saveAll(List.of(friend1, friend2));

		// when
		List<String> foundFriendPhoneNumbers = sut.findAllFriendPhoneNumbersByMyProfileId(myProfile.getId());

		// then
		assertThat(foundFriendPhoneNumbers.get(0))
			.isIn(friendProfile1.getPhoneNumber(), friendProfile2.getPhoneNumber());
		assertThat(foundFriendPhoneNumbers.get(1))
			.isIn(friendProfile1.getPhoneNumber(), friendProfile2.getPhoneNumber());
	}
}
