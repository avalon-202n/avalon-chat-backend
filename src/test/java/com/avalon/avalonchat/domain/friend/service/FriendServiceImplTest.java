package com.avalon.avalonchat.domain.friend.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.domain.friend.dto.FriendAddRequest;
import com.avalon.avalonchat.domain.friend.dto.FriendAddResponse;
import com.avalon.avalonchat.domain.friend.repository.FriendRepository;
import com.avalon.avalonchat.domain.profile.domain.BackgroundImage;
import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.domain.ProfileImage;
import com.avalon.avalonchat.domain.profile.repository.ProfileRepository;
import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.Password;
import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.repository.UserRepository;

@SpringBootTest
class FriendServiceImplTest {
	@Autowired
	private FriendServiceImpl friendService;
	@Autowired
	private FriendRepository friendRepository;
	@Autowired
	private ProfileRepository profileRepository;
	@Autowired
	private UserRepository userRepository;

	@Test
	@Transactional
	void 친구추가_성공() {
		// given
		String phoneNumber1 = "010-1234-5678";
		String phoneNumber2 = "010-8765-4321";
		String[] phoneNumbers = {phoneNumber1, phoneNumber2};
		FriendAddRequest request = new FriendAddRequest(phoneNumbers);

		User myUser = new User(Email.of("myuser@gmail.com"), Password.of("password"));
		User friendUser1 = new User(Email.of("frienduser1@gmail.com"), Password.of("password1"));
		User friendUser2 = new User(Email.of("frienduser2@gmail.com"), Password.of("password2"));

		User savedMyUser = userRepository.save(myUser);
		User savedFriendUser1 = userRepository.save(friendUser1);
		User savedFriendUser2 = userRepository.save(friendUser2);

		String phoneNumber = "01055110625";

		Profile myProfile = new Profile(savedMyUser, "bio", LocalDate.now(), "nickname", phoneNumber);
		Profile friendProfile1 = new Profile(savedFriendUser1, "bio1", LocalDate.now(), "nickname1", phoneNumber);
		Profile friendProfile2 = new Profile(savedFriendUser2, "bio2", LocalDate.now(), "nickname2", phoneNumber);
		friendProfile1.setPhoneNumber(phoneNumber1);
		friendProfile2.setPhoneNumber(phoneNumber2);

		String friendProfileUrl1 = "storage/url/profile_image1.png";
		String friendProfileUrl2 = "storage/url/profile_image2.png";
		String friendBackgroundUrl1 = "storage/url/background_image1.png";
		String friendBackgroundUrl2 = "storage/url/background_image2.png";

		ProfileImage friendProfileImage1 = new ProfileImage(friendProfile1, friendProfileUrl1);
		ProfileImage friendProfileImage2 = new ProfileImage(friendProfile2, friendProfileUrl2);
		BackgroundImage friendBackgroundImage1 = new BackgroundImage(friendProfile1, friendBackgroundUrl1);
		BackgroundImage friendBackgroundImage2 = new BackgroundImage(friendProfile2, friendBackgroundUrl2);

		friendProfile1.addProfileImage(friendProfileImage1);
		friendProfile1.addBackgroundImage(friendBackgroundImage1);
		friendProfile2.addProfileImage(friendProfileImage2);
		friendProfile2.addBackgroundImage(friendBackgroundImage2);

		Profile savedMyProfile = profileRepository.save(myProfile);
		profileRepository.save(friendProfile1);
		profileRepository.save(friendProfile2);

		// when
		List<FriendAddResponse> responses = friendService.addFriend(savedMyProfile.getId(), request);

		// then
		for (FriendAddResponse response : responses) {
			assertThat(response.getFriendProfileId()).isIn(
				Arrays.asList(friendProfile1.getId(), friendProfile2.getId()));
			assertThat(response.getNickname()).isIn(
				Arrays.asList(friendProfile1.getNickname(), friendProfile2.getNickname()));
			assertThat(response.getBio()).isIn(Arrays.asList(friendProfile1.getBio(), friendProfile2.getBio()));
			assertThat(response.getProfileImages()[0]).isIn(Arrays.asList(friendProfileUrl1, friendProfileUrl2));
			assertThat(response.getBackgroundImages()[0]).isIn(
				Arrays.asList(friendBackgroundUrl1, friendBackgroundUrl2));
			assertThat(response.getFriendStatus()).isEqualTo("NORMAL");
		}
	}
}
