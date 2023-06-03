package com.avalon.avalonchat.core.friend.application;

import static com.avalon.avalonchat.testsupport.DtoFixture.*;
import static com.avalon.avalonchat.testsupport.Fixture.*;
import static java.time.LocalDate.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.avalon.avalonchat.core.friend.domain.Friend;
import com.avalon.avalonchat.core.friend.domain.Friend.Status;
import com.avalon.avalonchat.core.friend.domain.FriendRepository;
import com.avalon.avalonchat.core.friend.dto.FriendEmailAddRequest;
import com.avalon.avalonchat.core.friend.dto.FriendEmailAddResponse;
import com.avalon.avalonchat.core.friend.dto.FriendPhoneNumberAddRequest;
import com.avalon.avalonchat.core.friend.dto.FriendPhoneNumberAddResponse;
import com.avalon.avalonchat.core.friend.dto.FriendStatusUpdateRequest;
import com.avalon.avalonchat.core.friend.dto.FriendStatusUpdateResponse;
import com.avalon.avalonchat.core.friend.dto.FriendSynchronizeRequest;
import com.avalon.avalonchat.core.friend.dto.FriendSynchronizeResponse;
import com.avalon.avalonchat.core.profile.domain.Profile;
import com.avalon.avalonchat.core.profile.domain.ProfileRepository;
import com.avalon.avalonchat.core.user.domain.Email;
import com.avalon.avalonchat.core.user.domain.Password;
import com.avalon.avalonchat.core.user.domain.User;
import com.avalon.avalonchat.core.user.domain.UserRepository;
import com.avalon.avalonchat.global.error.exception.BadRequestException;

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
	void 연락처_친구추가_성공() {
		// given
		User myUser = createUser("myuser@email.com", "passw0rd");
		User friendUser = createUser("frienduser@email.com", "passw0rd");
		userRepository.saveAll(List.of(myUser, friendUser));

		Profile myProfile = createProfile(
			myUser,
			"my bio",
			of(1999, 01, 23),
			"이철수",
			"010-1234-4321"
		);
		Profile friendProfile = createProfile(
			friendUser,
			"friend bio",
			of(1999, 07, 07),
			"홍길동",
			"010-1234-5678"
		);
		profileRepository.saveAll(List.of(myProfile, friendProfile));

		FriendPhoneNumberAddRequest request
			= friendPhoneNumberAddRequest("010-1234-5678", "홍길동");

		// when
		FriendPhoneNumberAddResponse response = sut.addFriendByPhoneNumber(myProfile.getId(), request);

		// then
		assertThat(response.getFriendProfileId()).isEqualTo(friendProfile.getId());
		assertThat(response.getFriendName()).isEqualTo(request.getFriendProfileNickname());
		assertThat(response.getBio()).isEqualTo(friendProfile.getBio());
		assertThat(response.getProfileImage()).isEqualTo(friendProfile.getLatestProfileImageUrl());
		assertThat(response.getStatus()).isEqualTo(Status.NORMAL);
	}

	@Test
	@Transactional
	void 연락처_친구추가_예외발생() {
		// given
		User myUser = createUser("myuser@email.com", "passw0rd");
		User friendUser = createUser("frienduser@email.com", "passw0rd");
		userRepository.saveAll(List.of(myUser, friendUser));

		Profile myProfile = createProfile(
			myUser,
			"my bio",
			of(1999, 01, 23),
			"이철수",
			"010-1234-4321"
		);
		Profile friendProfile = createProfile(
			friendUser,
			"friend bio",
			of(1999, 07, 07),
			"홍길동",
			"010-1234-5678"
		);
		profileRepository.saveAll(List.of(myProfile, friendProfile));

		Friend friend = createFriend(myProfile, friendProfile, "홍길동99");
		friendRepository.save(friend);

		FriendPhoneNumberAddRequest request
			= friendPhoneNumberAddRequest("010-1234-5678", "홍길동99");

		// when & then
		assertThatExceptionOfType(BadRequestException.class)
			.isThrownBy(() -> sut.addFriendByPhoneNumber(myProfile.getId(), request));
	}

	@Test
	@Transactional
	void 이메일_친구추가_성공() {
		// given
		User myUser = createUser("myuser@email.com", "passw0rd");
		User friendUser = createUser("frienduser@email.com", "passw0rd");
		userRepository.saveAll(List.of(myUser, friendUser));

		Profile myProfile = createProfile(
			myUser,
			"my bio",
			of(1999, 01, 23),
			"이철수",
			"010-1234-4321"
		);
		Profile friendProfile = createProfile(
			friendUser,
			"friend bio",
			of(1999, 07, 07),
			"홍길동",
			"010-1234-5678"
		);
		profileRepository.saveAll(List.of(myProfile, friendProfile));

		FriendEmailAddRequest request = friendEmailAddRequest("frienduser@email.com");

		// when
		FriendEmailAddResponse response = sut.addFriendByEmail(myProfile.getId(), request);

		// then
		assertThat(response.getFriendProfileId()).isEqualTo(friendProfile.getId());
		assertThat(response.getFriendName()).isEqualTo(friendProfile.getNickname());
		assertThat(response.getBio()).isEqualTo(friendProfile.getBio());
		assertThat(response.getProfileImage()).isEqualTo(friendProfile.getLatestProfileImageUrl());
		assertThat(response.getStatus()).isEqualTo(Status.NORMAL);
	}

	@Test
	@Transactional
	void 이메일_친구추가_예외발생() {
		// given
		User myUser = createUser("myuser@email.com", "passw0rd");
		User friendUser = createUser("frienduser@email.com", "passw0rd");
		userRepository.saveAll(List.of(myUser, friendUser));

		Profile myProfile = createProfile(
			myUser,
			"my bio",
			of(1999, 01, 23),
			"이철수",
			"010-1234-4321"
		);
		Profile friendProfile = createProfile(
			friendUser,
			"friend bio",
			of(1999, 07, 07),
			"홍길동",
			"010-1234-5678"
		);
		profileRepository.saveAll(List.of(myProfile, friendProfile));

		Friend friend = createFriend(myProfile, friendProfile, friendProfile.getNickname());
		friendRepository.save(friend);

		FriendEmailAddRequest request = friendEmailAddRequest("frienduser@email.com");

		// when & then
		assertThatExceptionOfType(BadRequestException.class)
			.isThrownBy(() -> sut.addFriendByEmail(myProfile.getId(), request));
	}

	@Test
	@Transactional
	void 친구동기화_성공() {
		// given
		User myUser = createUser("myuser@email.com", "passw0rd");
		User newFriendUser = createUser("newfriend@email.com", "passw0rd");
		User existFriendUser = createUser("existfriend@email.com", "passw0rd");
		userRepository.saveAll(List.of(myUser, newFriendUser, existFriendUser));

		Profile myProfile = createProfile(
			myUser, "my bio", of(1999, 01, 23),
			"이철수", "010-1122-3344"
		);
		Profile newFriendProfile = createProfile(
			newFriendUser, "new friend", of(1999, 02, 12),
			"홍길동", "010-1234-5678"
		);
		Profile existFriendProfile = createProfile(
			existFriendUser, "exist friend", of(1999, 12, 25),
			"김영희", "010-4321-1234"
		);
		profileRepository.saveAll(List.of(myProfile, newFriendProfile, existFriendProfile));

		Friend friend = createFriend(myProfile, existFriendProfile, "김영희99");
		friendRepository.save(friend);

		Map<String, String> friendsInfo = new HashMap<>();
		friendsInfo.put("010-1234-5678", "홍길동99");
		friendsInfo.put("010-4321-1234", "김영희99");

		FriendSynchronizeRequest request = friendSynchronizeRequest(friendsInfo);

		// when
		List<FriendSynchronizeResponse> responses = sut.synchronizeFriend(myProfile.getId(), request);

		// then
		assertThat(responses).hasSize(1);
		assertThat(responses.get(0).getFriendProfileId()).isEqualTo(newFriendProfile.getId());
		assertThat(responses.get(0).getFriendName()).isEqualTo(friendsInfo.get("010-1234-5678"));
		assertThat(responses.get(0).getBio()).isEqualTo(newFriendProfile.getBio());
		assertThat(responses.get(0).getProfileImage()).isEqualTo(newFriendProfile.getLatestProfileImageUrl());
		assertThat(responses.get(0).getStatus()).isEqualTo(Status.NORMAL);
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
		Friend friend = new Friend(savedMyProfile, savedFriendProfile, "friendNickname");
		friendRepository.save(friend);

		// given - ready for the request
		FriendStatusUpdateRequest request = new FriendStatusUpdateRequest(Status.BLOCKED);

		// when
		FriendStatusUpdateResponse response = sut.updateFriendStatus(myProfile.getId(),
			friendProfile.getId(), request);

		// then
		assertThat(response.getStatus()).isEqualTo(Status.BLOCKED);
	}
}
