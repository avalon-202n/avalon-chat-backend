package com.avalon.avalonchat.domain.friend.service;

import static java.time.LocalDate.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.domain.friend.domain.Friend;
import com.avalon.avalonchat.domain.friend.domain.Friend.Status;
import com.avalon.avalonchat.domain.friend.dto.FriendAddRequest;
import com.avalon.avalonchat.domain.friend.dto.FriendAddResponse;
import com.avalon.avalonchat.domain.friend.dto.FriendStatusUpdateRequest;
import com.avalon.avalonchat.domain.friend.dto.FriendStatusUpdateResponse;
import com.avalon.avalonchat.domain.friend.repository.FriendRepository;
import com.avalon.avalonchat.domain.profile.domain.Profile;
import com.avalon.avalonchat.domain.profile.repository.ProfileRepository;
import com.avalon.avalonchat.domain.user.domain.Email;
import com.avalon.avalonchat.domain.user.domain.Password;
import com.avalon.avalonchat.domain.user.domain.User;
import com.avalon.avalonchat.domain.user.repository.UserRepository;

@Transactional
@SpringBootTest
class FriendServiceImplTest {
	@Autowired
	private FriendServiceImpl sut;
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
		FriendAddRequest request = new FriendAddRequest(List.of("010-1234-5678", "010-8765-4321"));

		User myUser = new User(Email.of("myuser@gmail.com"), Password.of("password"));
		User friendUser1 = new User(Email.of("frienduser1@gmail.com"), Password.of("password1"));
		User friendUser2 = new User(Email.of("frienduser2@gmail.com"), Password.of("password2"));

		User savedMyUser = userRepository.save(myUser);
		User savedFriendUser1 = userRepository.save(friendUser1);
		User savedFriendUser2 = userRepository.save(friendUser2);

		Profile myProfile = new Profile(savedMyUser, "bio", now(), "myuser", "010-5511-0625");
		Profile friendProfile1 = new Profile(savedFriendUser1, "bio1", now(), "frienduser1", "010-1234-5678");
		Profile friendProfile2 = new Profile(savedFriendUser2, "bio2", now(), "frienduser2", "010-8765-4321");

		String friendProfileUrl1 = "storage/url/profile_image1.png";
		String friendProfileUrl2 = "storage/url/profile_image2.png";
		String friendBackgroundUrl1 = "storage/url/background_image1.png";
		String friendBackgroundUrl2 = "storage/url/background_image2.png";

		friendProfile1.addProfileImage("storage/url/profile_image1.png");
		friendProfile1.addBackgroundImage("storage/url/background_image1.png");
		friendProfile2.addProfileImage("storage/url/profile_image2.png");
		friendProfile2.addBackgroundImage("storage/url/background_image2.png");

		Profile savedMyProfile = profileRepository.save(myProfile);
		profileRepository.save(friendProfile1);
		profileRepository.save(friendProfile2);

		// when
		List<FriendAddResponse> responses = sut.addFriend(savedMyProfile.getId(), request);

		// then
		for (FriendAddResponse response : responses) {
			assertThat(response.getFriendProfileId()).isIn(friendProfile1.getId(), friendProfile2.getId());
			assertThat(response.getNickname()).isIn(friendProfile1.getNickname(), friendProfile2.getNickname());
			assertThat(response.getBio()).isIn(friendProfile1.getBio(), friendProfile2.getBio());
			assertThat(response.getProfileImages().get(0)).isIn(friendProfileUrl1, friendProfileUrl2);
			assertThat(response.getStatus()).isEqualTo(Status.NORMAL);
		}
	}

	@Test
	void 친구상태변경_성공() {
		// given - ready for users & profiles
		User myUser = new User(Email.of("myUser@gmail.com"), Password.of("myPassword"));
		Profile myProfile = new Profile(myUser, "myBio", LocalDate.now(), "myProfile", "01012345678");

		User friendUser = new User(Email.of("friendUser@gmail.com"), Password.of("friendPassword"));
		Profile friendProfile = new Profile(friendUser, "friendBio", LocalDate.now(), "friendNickname", "01012123434");

		userRepository.save(myUser);
		userRepository.save(friendUser);
		Profile savedMyProfile = profileRepository.save(myProfile);
		Profile savedFriendProfile = profileRepository.save(friendProfile);

		// given - ready for friend
		Friend friend = new Friend(savedMyProfile, savedFriendProfile);
		friendRepository.save(friend);

		// given - ready for the request
		FriendStatusUpdateRequest request = new FriendStatusUpdateRequest("BLOCKED");

		// when
		FriendStatusUpdateResponse response = sut.updateFriendStatus(myProfile.getId(),
			friendProfile.getId(), request);

		// then
		assertThat(response.getStatus()).isEqualTo(Status.BLOCKED);
	}
}
