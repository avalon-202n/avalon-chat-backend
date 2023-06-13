package com.avalon.avalonchat.core.profile.application;

import static com.avalon.avalonchat.testsupport.DtoFixture.*;
import static com.avalon.avalonchat.testsupport.Fixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.core.friend.domain.Friend;
import com.avalon.avalonchat.core.friend.domain.FriendRepository;
import com.avalon.avalonchat.core.profile.domain.Profile;
import com.avalon.avalonchat.core.profile.domain.ProfileRepository;
import com.avalon.avalonchat.core.profile.dto.BackgroundImageDeleteRequest;
import com.avalon.avalonchat.core.profile.dto.ProfileDetailedGetResponse;
import com.avalon.avalonchat.core.profile.dto.ProfileImageDeleteRequest;
import com.avalon.avalonchat.core.profile.dto.ProfileListGetResponse;
import com.avalon.avalonchat.core.profile.dto.ProfileUpdateRequest;
import com.avalon.avalonchat.core.profile.dto.ProfileUpdateResponse;
import com.avalon.avalonchat.core.user.application.PhoneNumberAuthCodeStore;
import com.avalon.avalonchat.core.user.application.UserService;
import com.avalon.avalonchat.core.user.domain.User;
import com.avalon.avalonchat.core.user.domain.UserRepository;
import com.avalon.avalonchat.global.error.exception.NotFoundException;

@Transactional
@SpringBootTest
class ProfileServiceImplTest {

	@Autowired
	private ProfileServiceImpl sut;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private PhoneNumberAuthCodeStore phoneNumberAuthKeyValueStore;

	@Autowired
	private FriendRepository friendRepository;

	@Test
	void profileId_로_profile_상세_조회_성공() {
		//given
		User user = createUser("hello@world.com", "password");
		User savedUser = userRepository.save(user);

		Profile profile = createProfile(user, "hi there", LocalDate.of(1997, 8, 21), "haha", "010-5511-0625");
		Profile savedProfile = profileRepository.save(profile);

		//when
		ProfileDetailedGetResponse response = sut.getDetailedById(savedProfile.getId());

		//then
		assertThat(response.getBio()).isEqualTo("hi there");
		assertThat(response.getNickname()).isEqualTo("haha");
	}

	@Test
	void profileId로_친구_profile_상세_조회_성공() {
		//given
		User user = createUser("hello@world.com", "password");
		User friendUser = createUser("friendUser1@world.com", "friendUser1");
		userRepository.save(user);
		userRepository.save(friendUser);

		Profile myProfile = createProfile(user, "hi there", LocalDate.of(1997, 8, 21), "haha", "010-5511-0625");
		Profile friendProfile = createProfile(
			friendUser, "I'm friend1", LocalDate.of(1998, 9, 22), "A_friend", "010-1212-3434"
		);
		profileRepository.save(myProfile);
		profileRepository.save(friendProfile);

		// given - ready for friends
		Friend friend = new Friend(myProfile, friendProfile, "A_friend");
		friendRepository.save(friend);

		//when
		ProfileDetailedGetResponse response = sut.getFriendDetailedById(myProfile.getId(), friendProfile.getId());

		//then
		assertThat(response.getBio()).isEqualTo("I'm friend1");
		assertThat(response.getNickname()).isEqualTo("A_friend");
	}

	@Test
	void 친구가_아닌_profileId로_profile_상세_조회_실패() {
		//given
		User user = createUser("hello@world.com", "password");
		User friendUser = createUser("friendUser1@world.com", "friendUser1");
		userRepository.save(user);
		userRepository.save(friendUser);

		Profile myProfile = createProfile(user, "hi there", LocalDate.of(1997, 8, 21), "haha", "010-5511-0625");
		Profile friendProfile = createProfile(
			friendUser, "I'm friend1", LocalDate.of(1998, 9, 22), "A_friend", "010-1212-3434"
		);
		profileRepository.save(myProfile);
		profileRepository.save(friendProfile);

		//when & then
		assertThatExceptionOfType(NotFoundException.class)
			.isThrownBy(() -> sut.getFriendDetailedById(myProfile.getId(), friendProfile.getId()));
	}

	@Test
	void myProfileId와_검색어로_friendProfiles_목록조회성공() {
		//given - ready for users
		User myUser = createUser("myUser@world.com", "myUserPassword");
		User friendUser1 = createUser("friendUser1@world.com", "friendUser1");
		User friendUser2 = createUser("friendUser2@world.com", "friendUser2");
		userRepository.save(myUser);
		userRepository.save(friendUser1);
		userRepository.save(friendUser2);

		// given - ready for profiles
		Profile myProfile = createProfile(
			myUser, "I'm myUser", LocalDate.of(1997, 8, 21), "my", "010-1234-5678"
		);
		Profile friendProfile1 = createProfile(
			friendUser1, "I'm friend1", LocalDate.of(1998, 9, 22), "A_friend", "010-1212-3434"
		);
		Profile friendProfile2 = createProfile(
			friendUser2, "I'm friend2", LocalDate.of(1999, 10, 23), "B_friend", "010-1111-2222"
		);
		friendProfile1.addProfileImage("url1");
		friendProfile1.addProfileImage("url2");
		friendProfile2.addProfileImage("url3");
		friendProfile2.addProfileImage("url4");
		Profile savedMyProfile = profileRepository.save(myProfile);
		profileRepository.save(friendProfile1);
		profileRepository.save(friendProfile2);

		// given - ready for friends
		Friend friend1 = new Friend(myProfile, friendProfile1, "A_friend");
		Friend friend2 = new Friend(myProfile, friendProfile2, "B_friend");
		friendRepository.save(friend1);
		friendRepository.save(friend2);

		// when
		List<ProfileListGetResponse> responses = sut.getListById(savedMyProfile.getId());

		// then
		assertThat(responses.size()).isEqualTo(2);
		assertThat(responses.get(0).getNickname()).isEqualTo("A_friend");
		assertThat(responses.get(1).getNickname()).isEqualTo("B_friend");
		assertThat(responses.get(0).getProfileImageUrl()).isEqualTo("url2");
		assertThat(responses.get(1).getProfileImageUrl()).isEqualTo("url4");
	}

	@Test
	void profile_이미지포함_수정성공() {
		// given
		User user = createUser("email@email.com", "password");
		Profile profile = createProfile(
			user,
			"bio",
			LocalDate.of(1999, 01, 01),
			"nickName",
			"010-1234-5678"
		);
		userRepository.save(user);
		profileRepository.save(profile);

		ProfileUpdateRequest request = profileUpdateRequest(
			LocalDate.of(1999, 01, 01),
			"nickName",
			"updated bio",
			"http://profile/image/url",
			"http://background/image/url"
		);

		// when
		ProfileUpdateResponse response = sut.updateProfile(profile.getId(), request);

		// then
		assertThat(response.getBirthDate()).isEqualTo(LocalDate.of(1999, 01, 01));
		assertThat(response.getNickname()).isEqualTo("nickName");
		assertThat(response.getBio()).isNotEqualTo("bio");
		assertThat(response.getBio()).isEqualTo("updated bio");

		assertThat(response.getProfileImageUrl()).isEqualTo("http://profile/image/url");
		assertThat(response.getBackgroundImageUrls().get(0)).isEqualTo("http://background/image/url");
	}

	@Test
	void profile_이미지제외_수정성공() {
		// given
		User user = createUser("email@email.com", "password");
		Profile profile = createProfile(
			user,
			"bio",
			LocalDate.of(1999, 01, 01),
			"nickName",
			"010-1234-5678"
		);
		userRepository.save(user);
		profileRepository.save(profile);

		ProfileUpdateRequest request = profileUpdateRequest(
			LocalDate.of(1999, 02, 02),
			"updated nickName",
			"bio",
			"",
			null
		);

		// when
		ProfileUpdateResponse response = sut.updateProfile(profile.getId(), request);

		// then
		assertThat(response.getBirthDate()).isNotEqualTo(LocalDate.of(1999, 01, 01));
		assertThat(response.getBirthDate()).isEqualTo(LocalDate.of(1999, 02, 02));
		assertThat(response.getNickname()).isNotEqualTo("nickName");
		assertThat(response.getNickname()).isEqualTo("updated nickName");
		assertThat(response.getBio()).isEqualTo("bio");

		assertThat(response.getProfileImageUrl()).isNull();
		assertThat(response.getBackgroundImageUrls()).hasSize(0);
	}

	@Test
	void 프로필이미지_삭제성공() {
		// given
		User user = createUser("test@email.com", "passw0rd");
		Profile profile = createProfile(user);

		profile.addProfileImage("http://profile/image/url1");
		profile.addProfileImage("http://profile/image/url2");
		profile.addProfileImage("http://profile/image/url3");
		profile.addProfileImage("http://profile/image/url4");
		profile.addProfileImage("http://profile/image/url5");

		userRepository.save(user);
		profileRepository.save(profile);

		ProfileImageDeleteRequest request = profileImageDeleteRequest("http://profile/image/url5");

		// when
		sut.deleteProfileImage(profile.getId(), request);

		// then
		assertThat(profile.getLatestProfileImageUrl()).isEqualTo("http://profile/image/url4");
		assertThat(profile.getProfileImages()).hasSize(4);
	}

	@Test
	void backgroundImage_삭제성공() {
		// given
		User user = createUser("test@email.com", "passw0rd");
		Profile profile = createProfile(user);

		profile.addBackgroundImage("http://background/image/url1");
		profile.addBackgroundImage("http://background/image/url2");
		profile.addBackgroundImage("http://background/image/url3");
		profile.addBackgroundImage("http://background/image/url4");
		profile.addBackgroundImage("http://background/image/url5");

		userRepository.save(user);
		profileRepository.save(profile);

		BackgroundImageDeleteRequest request
			= backgroundImageDeleteRequest("http://background/image/url5");

		// when
		sut.deleteBackgroundImage(profile.getId(), request);

		// then
		assertThat(profile.getBackgroundImages()).hasSize(4);
	}
}

