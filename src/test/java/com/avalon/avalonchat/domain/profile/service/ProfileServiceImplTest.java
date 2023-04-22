package com.avalon.avalonchat.domain.profile.service;

import static com.avalon.avalonchat.testsupport.Fixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.domain.friend.domain.Friend;
import com.avalon.avalonchat.domain.friend.repository.FriendRepository;
import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.domain.ProfileImage;
import com.avalon.avalonchat.domain.profile.dto.ProfileAddRequest;
import com.avalon.avalonchat.domain.profile.dto.ProfileAddResponse;
import com.avalon.avalonchat.domain.profile.dto.ProfileDetailedGetResponse;
import com.avalon.avalonchat.domain.profile.dto.ProfileListGetResponse;
import com.avalon.avalonchat.domain.profile.repository.ProfileRepository;
import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.Password;
import com.avalon.avalonchat.domain.user.domain.PhoneNumberAuthenticationCode;
import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.dto.PhoneNumberAuthenticationCheckRequest;
import com.avalon.avalonchat.domain.user.repository.PhoneNumberAuthenticationRepository;
import com.avalon.avalonchat.domain.user.repository.UserRepository;
import com.avalon.avalonchat.domain.user.service.MessageService;
import com.avalon.avalonchat.domain.user.service.UserService;
import com.avalon.avalonchat.global.error.exception.BadRequestException;
import com.avalon.avalonchat.testsupport.base.BaseTestContainerTest;

@Transactional
@SpringBootTest
class ProfileServiceImplTest extends BaseTestContainerTest {

	@Autowired
	private ProfileServiceImpl sut;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserService userService;

	@Autowired
	private PhoneNumberAuthenticationRepository phoneNumberAuthenticationRepository;

	@Autowired
	private FriendRepository friendRepository;

	@Test
	@Disabled("TODO - add mocking or add new NullMessageService for test")
	void addProfile_성공() {
		// given - authenticate phone number
		String certificationCode = RandomStringUtils.randomNumeric(6);
		String toPhoneNumber = "01055110625";

		messageService.sendAuthenticationCode(toPhoneNumber, certificationCode);

		PhoneNumberAuthenticationCode entity = new PhoneNumberAuthenticationCode(toPhoneNumber, certificationCode);
		phoneNumberAuthenticationRepository.save(entity);

		PhoneNumberAuthenticationCheckRequest authenticationRequest = new PhoneNumberAuthenticationCheckRequest(
			toPhoneNumber, certificationCode);

		userService.checkPhoneNumberAuthentication(authenticationRequest);

		// given - ready for the request
		User user = new User(Email.of("email@gmail.com"), Password.of("password"));
		User savedUser = userRepository.save(user);

		LocalDate birthDate = LocalDate.now();
		String nickname = "nickname";
		String bio = "bio";
		String profileImageUrl = "profileImageUrl";
		String backgroundImageUrl = "backgroundImageUrl";
		ProfileAddRequest request = new ProfileAddRequest(birthDate, nickname, bio, profileImageUrl,
			backgroundImageUrl, toPhoneNumber);

		// when
		ProfileAddResponse response = sut.addProfile(savedUser.getId(), request);

		// then
		assertThat(response.getBirthDate()).isEqualTo(birthDate);
		assertThat(response.getNickname()).isEqualTo(nickname);
		assertThat(response.getBio()).isEqualTo(bio);
		assertThat(response.getProfileImages().get(0)).isEqualTo(profileImageUrl);
		assertThat(response.getBackgroundImages().get(0)).isEqualTo(backgroundImageUrl);
		assertThat(response.getPhoneNumber()).isEqualTo(toPhoneNumber);
	}

	@Test
	void addProfile_핸드폰인증되지않은사용자_예외던지기_성공() {
		// given - send user certificationCode with no checking
		String certificationCode = RandomStringUtils.randomNumeric(6);
		String toPhoneNumber = "01055110625";

		messageService.sendAuthenticationCode(toPhoneNumber, certificationCode);

		PhoneNumberAuthenticationCode entity = new PhoneNumberAuthenticationCode(toPhoneNumber, certificationCode);
		phoneNumberAuthenticationRepository.save(entity);

		// given - ready for the request
		User user = new User(Email.of("email@gmail.com"), Password.of("password"));
		User savedUser = userRepository.save(user);

		LocalDate birthDate = LocalDate.now();
		String nickname = "nickname";
		String bio = "bio";
		String profileImageUrl = "profileImageUrl";
		String backgroundImageUrl = "backgroundImageUrl";
		ProfileAddRequest request = new ProfileAddRequest(birthDate, nickname, bio, profileImageUrl,
			backgroundImageUrl, toPhoneNumber);

		// when & then
		assertThatExceptionOfType(BadRequestException.class)
			.isThrownBy(() -> sut.addProfile(savedUser.getId(), request));
	}

	@Test
	void userId_로_profileId_조회_성공() {
		//given
		User user = createUser("hello@world.com", "password");
		User savedUser = userRepository.save(user);

		Profile profile = createProfile(user, "hi there", LocalDate.of(1997, 8, 21), "haha", "01055110625");
		Profile savedProfile = profileRepository.save(profile);

		//when
		long foundProfileId = sut.getProfileIdByUserId(savedUser.getId());

		//then
		assertThat(foundProfileId).isEqualTo(savedProfile.getId());
	}

	@Test
	void profileId_로_profile_상세_조회_성공() {
		//given
		User user = createUser("hello@world.com", "password");
		User savedUser = userRepository.save(user);

		Profile profile = createProfile(user, "hi there", LocalDate.of(1997, 8, 21), "haha", "01055110625");
		Profile savedProfile = profileRepository.save(profile);

		//when
		ProfileDetailedGetResponse response = sut.getDetailedById(savedProfile.getId());

		//then
		assertThat(response.getBio()).isEqualTo("hi there");
		assertThat(response.getNickname()).isEqualTo("haha");
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
			myUser, "I'm myUser", LocalDate.of(1997, 8, 21), "my", "01012345678"
		);
		Profile friendProfile1 = createProfile(
			friendUser1, "I'm friend1", LocalDate.of(1998, 9, 22), "A_friend", "01012123434"
		);
		Profile friendProfile2 = createProfile(
			friendUser2, "I'm friend2", LocalDate.of(1999, 10, 23), "B_friend", "01011112222"
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
		profileRepository.save(friendProfile1);
		profileRepository.save(friendProfile2);

		// given - ready for friends
		Friend friend1 = new Friend(myProfile, friendProfile1);
		Friend friend2 = new Friend(myProfile, friendProfile2);
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

	@AfterEach
	void tearDown() {
		phoneNumberAuthenticationRepository.deleteAll();
	}
}

